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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
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
    public Optional<Session> initSession(SessionRequest.SelectedSimulationSessionRequest request) throws SessionInitializationFailedException {
        var adaptorConfigs = getAdaptors().stream()
                .filter(config -> request.requestedSimulations().contains(config.getName()))
                .toList();
        var acquiredSessions = tryObtainAdaptorSessions(adaptorConfigs, Integer.MAX_VALUE);
        var aggregatedSession = constructAggregatedSession(acquiredSessions);
        return Optional.of(aggregatedSession);
    }

    @Override
    public Optional<Session> initSession(SessionRequest.AnySimulationSessionRequest request) throws SessionInitializationFailedException {
        var acquiredSessions = tryObtainAdaptorSessions(getAdaptors(), request.n());
        var aggregatedSession = constructAggregatedSession(acquiredSessions);
        return Optional.of(aggregatedSession);
    }

    public String deleteSession() {
        return null;
    }

    // private region methods

    private List<ServiceRegistrationConfigDTO> getAdaptors() {
        return serviceRegistryClient.getRegisteredAdaptors()
                .stream()
                .toList();
    }


    private List<AdaptorSession> tryObtainAdaptorSessions(List<ServiceRegistrationConfigDTO> requestedSimulations, int maximumSimulations){
        List<AdaptorSession> sessions = new ArrayList<>();
        for(var config : requestedSimulations){
            if(sessions.size() >= maximumSimulations)
                break;

            Optional<String> sessionKey = adaptorClient.getSession(config);
            if(sessionKey.isPresent()){
                sessions.add(AdaptorSession.builder()
                        .adaptorName(config.getName())
                        .sessionKey(sessionKey.get())
                        .build());
            }
        }
        return sessions;
    }

    private Session constructAggregatedSession(List<AdaptorSession> adaptorSessions) throws SessionInitializationFailedException {
        if(adaptorSessions.isEmpty()){
            throw new SessionInitializationFailedException("Could not obtain a single session");
        }
        Session session = Session.builder()
                .sessionKey(UUID.randomUUID())
                .state(SessionState.OPEN)
                .build();
        adaptorSessions.forEach(session::addAdaptorSession);
        return session;
    }
}
