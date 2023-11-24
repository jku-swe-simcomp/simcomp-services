package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequestVisitor;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionStateDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SessionService implements SessionRequestVisitor {
    private final SessionRepository sessionRepository;
    private final AdaptorSessionRepository adaptorSessionRepository;
    private final ServiceRegistryClient serviceRegistryClient;
    private final AdaptorClient adaptorClient;
    public SessionService(SessionRepository sessionRepository,
                          AdaptorClient adaptorClient,
                          ServiceRegistryClient serviceRegistryClient,
                          AdaptorSessionRepository adaptorSessionRepository) {
        this.sessionRepository = sessionRepository;
        this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorClient = adaptorClient;
        this.adaptorSessionRepository = adaptorSessionRepository;
    }

    @Override
    public Session initSession(SessionRequest.SelectedSimulationTypesSessionRequest request) throws SessionInitializationFailedException {
        var adaptorConfigs = getRegisteredAdaptors().stream()
                .filter(config -> request.requestedSimulationTypes().contains(config.getName()))
                .toList();
        log.debug("Filtered list of available adaptors: {}", adaptorConfigs);
        return requestAdaptorSessionsAndConstructAndPersistAggregatedSession(adaptorConfigs, Integer.MAX_VALUE);
    }

    @Override
    public Session initSession(SessionRequest.AnySimulationSessionRequest request) throws SessionInitializationFailedException {
        return requestAdaptorSessionsAndConstructAndPersistAggregatedSession(getRegisteredAdaptors(), request.n());
    }

    @Override
    public Session initSession(SessionRequest.SelectedSimulationInstancesSessionRequest request) throws SessionInitializationFailedException {
        var adaptorConfigsToRequestedInstanceId = getRegisteredAdaptors().stream()
                .filter(config -> request.requestedSimulationInstances().containsKey(config.getName()))
                .collect(Collectors.toMap(Function.identity(), config -> request.requestedSimulationInstances().get(config.getName())));

        List<AdaptorSession> sessions = new ArrayList<>();
        for(var entry : adaptorConfigsToRequestedInstanceId.entrySet()){
            Optional<String> sessionKey = adaptorClient.getSession(entry.getKey(), entry.getValue());
            sessionKey.ifPresent(s -> sessions.add(initAdaptorSession(s, entry.getKey().getName(), entry.getValue())));
        }

        if(sessions.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain a single session.");
        }
        var aggregatedSession = constructAggregatedSession(sessions);
        return sessionRepository.save(aggregatedSession);
    }

    public void closeSession(UUID aggregatedSessionKey) throws NotFoundException, BadRequestException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(aggregatedSessionKey);
        if(session.getState().equals(SessionState.CLOSED)){
            throw new BadRequestException("Session %s already closed".formatted(aggregatedSessionKey));
        }
        closeAdaptorSessions(aggregatedSessionKey);
        sessionRepository.updateSessionStateBySessionKey(aggregatedSessionKey, SessionState.CLOSED);
        log.debug("Closed session {}", aggregatedSessionKey.toString());
        //TODO: RESET_TO_HOME before closing adaptor-sessions? Or responsibility of the adaptor?
    }

    public SessionStateDTO getSessionState(UUID sessionKey) throws NotFoundException{
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
        return new SessionStateDTO(session.getSessionKey().toString(), session.getState(),
                session.getAdaptorSessions().stream()
                        .collect(Collectors.toMap(AdaptorSession::getAdaptorName, AdaptorSession::getState, (state1, state2) -> state1)));

    }

    public void addAdaptorSessionToAggregatedSession(UUID sessionKey, String adaptorName) throws BadRequestException, NotFoundException, SessionInitializationFailedException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
        if(session.getState().equals(SessionState.CLOSED)){
            throw new BadRequestException("Session %s already closed".formatted(sessionKey));
        }
        if(session.getAdaptorSessions().stream().map(AdaptorSession::getAdaptorName).anyMatch(name -> name.equals(adaptorName))){
            throw new BadRequestException("Simulation %s already part of session %s. You can manually close and reopen the simulation-session to initialize a new simulation-session".formatted(adaptorName, sessionKey));
        }

        var adaptorConfigs = getRegisteredAdaptors().stream()
                .filter(config -> config.getName().equals(adaptorName))
                .toList();

        if(adaptorConfigs.isEmpty()) {
            throw new NotFoundException("Simulation %s not registered".formatted(adaptorName));
        }

        var optAdaptorSessionKey = adaptorClient.getSession(adaptorConfigs.get(0));

        if(optAdaptorSessionKey.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain session for simulation %s".formatted(adaptorName));
        }

        AdaptorSession adaptorSession = initAdaptorSession(optAdaptorSessionKey.get(), adaptorName, null);
        session.addAdaptorSession(adaptorSession);
        sessionRepository.save(session);
    }

    public void closeAdaptorSessionOfAggregateSession(UUID sessionKey, String adaptorName) throws BadRequestException, NotFoundException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
        if(session.getState().equals(SessionState.CLOSED)){
            throw new BadRequestException("Session %s already closed".formatted(sessionKey));
        }
        AdaptorSession adaptorSession = session.getAdaptorSessions().stream()
                .filter(adaptorSession1 -> adaptorSession1.getAdaptorName().equals(adaptorName))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Simulation %s not part of session %s".formatted(adaptorName, sessionKey)));

        if(adaptorSession.getState() == SessionState.CLOSED){
            throw new BadRequestException("Simulation %s already closed".formatted(adaptorName));
        }
        closeAdaptorSession(adaptorSession, getRegisteredAdaptors());
    }

    public void reopenAdaptorSessionOfAggregateSession(UUID sessionKey, String adaptorName) throws BadRequestException, SessionInitializationFailedException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
        if(session.getState().equals(SessionState.CLOSED)){
            throw new BadRequestException("Session %s already closed".formatted(sessionKey));
        }
        AdaptorSession adaptorSession = session.getAdaptorSessions().stream()
                .filter(adaptorSession1 -> adaptorSession1.getAdaptorName().equals(adaptorName))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Simulation %s not part of session %s".formatted(adaptorName, sessionKey)));

        if(adaptorSession.getState().equals(SessionState.OPEN)){
            throw new BadRequestException("Simulation %s already open. Close it before trying to reopen a new one".formatted(adaptorName));
        }

        var adaptorConfigs = getRegisteredAdaptors().stream()
                .filter(config -> config.getName().equals(adaptorName))
                .toList();

        if(adaptorConfigs.isEmpty()) {
            throw new SessionInitializationFailedException("Simulation %s not registered".formatted(adaptorName));
        }

        Optional<String> optAdaptorSessionKey = Optional.empty();
        if(adaptorSession.getInstanceId() != null){
            optAdaptorSessionKey = adaptorClient.getSession(adaptorConfigs.get(0), adaptorSession.getInstanceId());
        }else{
            optAdaptorSessionKey = adaptorClient.getSession(adaptorConfigs.get(0));
        }

        if(optAdaptorSessionKey.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain session for instance %s of simulation %s".formatted(adaptorSession.getInstanceId(), adaptorName));
        }

        adaptorSession.setSessionKey(optAdaptorSessionKey.get());
        adaptorSession.setState(SessionState.OPEN);
        sessionRepository.save(session);
        // TODO: reconstruct latest known state of adaptor session
    }
    // private region methods

    private Session requestAdaptorSessionsAndConstructAndPersistAggregatedSession(List<ServiceRegistrationConfigDTO> adaptorConfigs, Integer maximumNumberOfSimulations) throws SessionInitializationFailedException {
        var acquiredSessions = tryObtainAdaptorSessions(adaptorConfigs, maximumNumberOfSimulations);
        var aggregatedSession = constructAggregatedSession(acquiredSessions);
        return sessionRepository.save(aggregatedSession);
    }

    private List<AdaptorSession> tryObtainAdaptorSessions(List<ServiceRegistrationConfigDTO> requestedSimulations, int maximumSimulations){
        List<AdaptorSession> sessions = new ArrayList<>();
        for(var config : requestedSimulations){
            if(sessions.size() >= maximumSimulations)
                break;

            tryAddAdaptorSession(config, sessions);
        }
        return sessions;
    }

    private void tryAddAdaptorSession(ServiceRegistrationConfigDTO config, List<AdaptorSession> sessions) {
        Optional<String> sessionKey = adaptorClient.getSession(config);
        sessionKey.ifPresent(s -> sessions.add(initAdaptorSession(s, config.getName(), null)));
    }

    private Session constructAggregatedSession(List<AdaptorSession> adaptorSessions) throws SessionInitializationFailedException {
        if(adaptorSessions.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain a single session");
        }
        log.debug("Aggregating adaptor sessions: {}", adaptorSessions);
        Session session = initNewSessionWithUUID();
        adaptorSessions.forEach(session::addAdaptorSession);
        log.debug("Aggregated session: {}", session);
        return session;
    }

    private Session initNewSessionWithUUID() {
        return Session.builder()
                .sessionKey(UUID.randomUUID())
                .state(SessionState.OPEN)
                .build();
    }

    private AdaptorSession initAdaptorSession(String key, String name, String instanceId){
        return AdaptorSession.builder()
                .sessionKey(key)
                .adaptorName(name)
                .instanceId(instanceId)
                .state(SessionState.OPEN)
                .build();
    }

    private List<ServiceRegistrationConfigDTO> getRegisteredAdaptors() {
        return serviceRegistryClient.getRegisteredAdaptors();
    }

    private void closeAdaptorSessions(UUID aggregatedSessionKey) {
        var adaptorConfigs = getRegisteredAdaptors();
        for(var adaptorSession : adaptorSessionRepository.findBySessionSessionKey(aggregatedSessionKey)){
            closeAdaptorSession(adaptorSession, adaptorConfigs);
        }
    }

    private void closeAdaptorSession(AdaptorSession adaptorSession, List<ServiceRegistrationConfigDTO> adaptorConfigs){
        adaptorConfigs.stream()
                .filter(config -> config.getName().equals(adaptorSession.getAdaptorName()))
                .findFirst()
                .ifPresent(serviceRegistrationConfigDTO -> adaptorClient.closeSession(serviceRegistrationConfigDTO, adaptorSession.getSessionKey()));
        adaptorSessionRepository.updateSessionStateBySessionKey(adaptorSession.getId(), SessionState.CLOSED);
    }

}
