package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequestVisitor;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionStateDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.rest.exception.BadRequestException;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SessionService implements SessionRequestVisitor {
    private final SessionRepository sessionRepository;
    private final ServiceRegistryClient serviceRegistryClient;
    private final AdaptorClient adaptorClient;
    public SessionService(SessionRepository sessionRepository,
                          AdaptorClient adaptorClient,
                          ServiceRegistryClient serviceRegistryClient) {
        this.sessionRepository = sessionRepository;
        this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorClient = adaptorClient;
    }

    @Override
    public Session initSession(SessionRequest.SelectedSimulationSessionRequest request) throws SessionInitializationFailedException {
        var adaptorConfigs = getRegisteredAdaptors().stream()
                .filter(config -> request.requestedSimulations().contains(config.getName()))
                .toList();
        log.debug("Filtered list of available adaptors: {}", adaptorConfigs);
        return getAdaptorSessionsAndConstructAndPersistAggregatedSession(adaptorConfigs, Integer.MAX_VALUE);
    }

    @Override
    public Session initSession(SessionRequest.AnySimulationSessionRequest request) throws SessionInitializationFailedException {
        return getAdaptorSessionsAndConstructAndPersistAggregatedSession(getRegisteredAdaptors(), request.n());
    }

    public void closeSession(UUID aggregatedSessionKey) throws NotFoundException{
        sessionRepository.findBySessionKeyOrElseThrow(aggregatedSessionKey);
        closeAdaptorSessions(aggregatedSessionKey);
        sessionRepository.updateSessionStateBySessionKey(aggregatedSessionKey, SessionState.CLOSED);
        log.debug("Closed session {}", aggregatedSessionKey.toString());
    }

    public SessionStateDTO getSessionState(UUID sessionKey) throws NotFoundException{
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
        return new SessionStateDTO(session.getSessionKey().toString(), session.getState(),
                session.getAdaptorSessions().stream()
                        .collect(Collectors.toMap(AdaptorSession::getAdaptorName, AdaptorSession::getState, (state1, state2) -> state1)));

    }

    public void addAdaptorSessionToAggregatedSession(UUID sessionKey, String adaptorName) throws BadRequestException, NotFoundException, SessionInitializationFailedException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
        if(session.getAdaptorSessions().stream().map(AdaptorSession::getAdaptorName).anyMatch(name -> name.equals(adaptorName))){
            throw new BadRequestException("Simulation %s already part of session %s".formatted(adaptorName, sessionKey));
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

        AdaptorSession adaptorSession = initAdaptorSession(optAdaptorSessionKey.get(), adaptorName);
        session.addAdaptorSession(adaptorSession);
    }

    public void closeAdaptorSessionOfAggregateSession(UUID sessionKey, String adaptorName) throws BadRequestException, NotFoundException {
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionKey);
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
        AdaptorSession adaptorSession = session.getAdaptorSessions().stream()
                .filter(adaptorSession1 -> adaptorSession1.getAdaptorName().equals(adaptorName))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("Simulation %s not part of session %s".formatted(adaptorName, sessionKey)));

        if(adaptorSession.getState().equals(SessionState.OPEN)){
            throw new BadRequestException("Simulation %s already open".formatted(adaptorName));
        }

        var adaptorConfigs = getRegisteredAdaptors().stream()
                .filter(config -> config.getName().equals(adaptorName))
                .toList();

        if(adaptorConfigs.isEmpty()) {
            throw new SessionInitializationFailedException("Simulation %s not registered".formatted(adaptorName));
        }

        var optAdaptorSessionKey = adaptorClient.getSession(adaptorConfigs.get(0));

        if(optAdaptorSessionKey.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain a session for simulation %s".formatted(adaptorName));
        }

        adaptorSession.setSessionKey(optAdaptorSessionKey.get());
        adaptorSession.setState(SessionState.OPEN);
        sessionRepository.save(session);
    }
    // private region methods

    private Session getAdaptorSessionsAndConstructAndPersistAggregatedSession(List<ServiceRegistrationConfigDTO> adaptorConfigs, Integer maximumNumberOfSimulations) throws SessionInitializationFailedException {
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
        sessionKey.ifPresent(s -> sessions.add(initAdaptorSession(s, config.getName())));
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

    private AdaptorSession initAdaptorSession(String key, String name){
        return AdaptorSession.builder()
                .sessionKey(key)
                .adaptorName(name)
                .state(SessionState.OPEN)
                .build();
    }

    private List<ServiceRegistrationConfigDTO> getRegisteredAdaptors() {
        return serviceRegistryClient.getRegisteredAdaptors();
    }

    private void closeAdaptorSessions(UUID aggregatedSessionKey) {
        var adaptorConfigs = getRegisteredAdaptors();
        var session = sessionRepository.findBySessionKeyOrElseThrow(aggregatedSessionKey);
        for(var adaptorSession : session.getAdaptorSessions()){
            closeAdaptorSession(adaptorSession, adaptorConfigs);
        }
    }

    private void closeAdaptorSession(AdaptorSession adaptorSession, List<ServiceRegistrationConfigDTO> adaptorConfigs){
        adaptorConfigs.stream()
                .filter(config -> config.getName().equals(adaptorSession.getAdaptorName()))
                .findFirst()
                .ifPresent(serviceRegistrationConfigDTO -> adaptorClient.closeSession(serviceRegistrationConfigDTO, adaptorSession.getSessionKey()));
        sessionRepository.updateAdaptorSessionStateBySessionKey(adaptorSession.getSessionKey(), SessionState.CLOSED);
    }

}
