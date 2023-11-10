package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequestVisitor;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.model.SessionState;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        var adaptorConfigs = getAdaptors().stream()
                .filter(config -> request.requestedSimulations().contains(config.getName()))
                .toList();
        log.debug("Filtered list of available adaptors: {}", adaptorConfigs);
        return getAdaptorSessionsAndConstructAndPersistAggregatedSession(adaptorConfigs, Integer.MAX_VALUE);
    }

    @Override
    public Session initSession(SessionRequest.AnySimulationSessionRequest request) throws SessionInitializationFailedException {
        return getAdaptorSessionsAndConstructAndPersistAggregatedSession(getAdaptors(), request.n());
    }

    public void closeSession(UUID key) {
        closeAdaptorSessions(key);
        sessionRepository.updateSessionStateBySessionKey(key, SessionState.CLOSED);
        log.debug("Closed session {}", key.toString());
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

            Optional<String> sessionKey = adaptorClient.getSession(config);
            sessionKey.ifPresent(s -> sessions.add(AdaptorSession.builder()
                    .adaptorName(config.getName())
                    .sessionKey(s)
                    .build()));
        }
        return sessions;
    }

    private Session constructAggregatedSession(List<AdaptorSession> adaptorSessions) throws SessionInitializationFailedException {
        if(adaptorSessions.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain a single session");
        }
        log.debug("Aggregating adaptor sessions: {}", adaptorSessions);
        Session session = Session.builder()
                .sessionKey(UUID.randomUUID())
                .state(SessionState.OPEN)
                .build();
        adaptorSessions.forEach(session::addAdaptorSession);
        log.debug("Aggregated session: {}", session);
        return session;
    }

    private List<ServiceRegistrationConfigDTO> getAdaptors() {
        return serviceRegistryClient.getRegisteredAdaptors()
                .stream()
                .toList();
    }

    private void closeAdaptorSessions(UUID key) {
        var adaptorConfigs = getAdaptors();
        var optionalSession = sessionRepository.findBySessionKey(key);
        if(optionalSession.isPresent()){
            var session = optionalSession.get();
            for(var adaptorSession : session.getAdaptorSessions()){
                closeAdaptorSession(adaptorSession, adaptorConfigs);
            }
        }
    }

    private void closeAdaptorSession(AdaptorSession adaptorSession, List<ServiceRegistrationConfigDTO> adaptorConfigs){
        adaptorConfigs.stream()
                .filter(config -> config.getName().equals(adaptorSession.getAdaptorName()))
                .findFirst()
                .ifPresent(serviceRegistrationConfigDTO -> adaptorClient.closeSession(serviceRegistrationConfigDTO, adaptorSession.getSessionKey()));

    }

}
