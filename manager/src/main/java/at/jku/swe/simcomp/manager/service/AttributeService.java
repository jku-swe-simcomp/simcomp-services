package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AttributeService {
    private final AdaptorSessionRepository adaptorSessionRepository;
    private final ServiceRegistryClient serviceRegistryClient;
    private final AdaptorClient adaptorClient;
    private final SessionRepository sessionRepository;
    private final KinematicsService kinematicsService;
    private final Boolean isDirectKinematicsEnabled;

    public AttributeService(ServiceRegistryClient serviceRegistryClient,
                            AdaptorClient adaptorClient,
                            SessionRepository sessionRepository,
                            AdaptorSessionRepository adaptorSessionRepository,
                            KinematicsService kinematicsService,
                            @Value("${application.kinematics.direct.enabled}") Boolean isDirectKinematicsEnabled) {
        this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorClient = adaptorClient;
        this.sessionRepository = sessionRepository;
        this.adaptorSessionRepository = adaptorSessionRepository;
        this.kinematicsService = kinematicsService;
        this.isDirectKinematicsEnabled = Objects.requireNonNullElse(isDirectKinematicsEnabled, true);
    }

    public Map<String, AttributeValue> getAttributeValues(UUID sessionId,
                                                          AttributeKey attributeKey){
        return isDirectKinematicsEnabled ? switch(attributeKey){
            case POSE, POSITION, ORIENTATION -> getAttributeValuesBasedOnKinematics(sessionId, attributeKey);
            default -> fetchAttributeValuesFromAdaptors(sessionId, attributeKey);
        } : fetchAttributeValuesFromAdaptors(sessionId, attributeKey);
    }

    private Map<String, AttributeValue> getAttributeValuesBasedOnKinematics(UUID sessionId,
                                                                         AttributeKey attributeKey){
        Map<String, AttributeValue> jointPositionValues = fetchAttributeValuesFromAdaptors(sessionId, AttributeKey.JOINT_POSITIONS);
        return jointPositionValues.entrySet().stream()
                .map(e -> {
                    if(e.getValue() instanceof AttributeValue.JointPositions p){
                        AttributeValue.Pose pose = kinematicsService.jointPositionsToPose(p);
                        AttributeValue resultValue = switch(attributeKey){
                            case POSITION -> new AttributeValue.Position(pose.pose().getPosition());
                            case ORIENTATION -> new AttributeValue.Orientation(pose.pose().getOrientation());
                            default -> pose;
                        };
                        return Map.entry(e.getKey(), resultValue);
                    }
                    return e;
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    private Map<String, AttributeValue> fetchAttributeValuesFromAdaptors(UUID sessionId,
                                                          AttributeKey attributeKey){
        log.info("Getting attribute values for session {} and attribute key {}", sessionId, attributeKey);
        Session session = sessionRepository.findBySessionKeyOrElseThrow(sessionId);
        List<AdaptorSession> sessions = session.getAdaptorSessions().stream()
                .filter(adaptorSession -> adaptorSession.getState() == SessionState.OPEN)
                .toList();
        List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs = serviceRegistryClient.getRegisteredAdaptors();

        Map<AdaptorSession, Optional<ServiceRegistrationConfigDTO>> sessionToConfigs = sessions.stream()
                .collect(Collectors.toMap(Function.identity(),
                        adaptorSession -> findMatchingConfig(adaptorSession, registeredAdaptorConfigs)));

        Map<String, AttributeValue> attributeValues = new HashMap<>();
        for(var entry : sessionToConfigs.entrySet()){
            AdaptorSession adaptorSession = entry.getKey();
            if (entry.getValue().isEmpty()) {
                log.info("No matching adaptor config found for {} of session {}", adaptorSession.getAdaptorName(), sessionId);
                attributeValues.put(adaptorSession.getAdaptorName(), null);
                continue;
            }
            ServiceRegistrationConfigDTO config = entry.getValue().get();
            Optional<AttributeValue> value = Optional.empty();
            try {
                value = adaptorClient.getAttributeValue(adaptorSession.getSessionKey(), attributeKey, config);
                log.info("Obtained attribute value {} for session {} from {} and attribute key {}", value, adaptorSession.getAdaptorName(), sessionId, attributeKey);
            } catch (SessionNotValidException e) {
                log.warn("Session {} of {} is not valid anymore. Closing it.", adaptorSession.getAdaptorName(), session.getSessionKey());
                adaptorSessionRepository.updateSessionStateById(adaptorSession.getId(), SessionState.CLOSED);
            }
            attributeValues.put(adaptorSession.getAdaptorName(), value.orElse(null));
        }
        return attributeValues;
    }


    private Optional<ServiceRegistrationConfigDTO> findMatchingConfig(AdaptorSession session,
                                                                      List<ServiceRegistrationConfigDTO> registeredAdaptorConfigs) {
        return registeredAdaptorConfigs.stream()
                .filter(config -> config.getName().equals(session.getAdaptorName()))
                .findFirst();
    }
}
