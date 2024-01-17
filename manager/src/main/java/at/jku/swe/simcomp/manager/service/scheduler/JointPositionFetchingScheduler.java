package at.jku.swe.simcomp.manager.service.scheduler;


import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.JointPositions;
import at.jku.swe.simcomp.manager.domain.repository.AdaptorSessionRepository;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This scheduler fetches the joint positions for all open adaptor sessions.
 */
@Component
@Slf4j
public class JointPositionFetchingScheduler {
    private final AdaptorSessionRepository adaptorSessionRepository;
    private final AdaptorClient adaptorClient;
    private final ServiceRegistryClient serviceRegistryClient;

    /**
     * Constructor
     * @param adaptorSessionRepository repository for adaptor sessions
     * @param adaptorClient client for adaptor communication
     * @param serviceRegistryClient client for service registry communication
     */
    public JointPositionFetchingScheduler(AdaptorSessionRepository adaptorSessionRepository,
                                          AdaptorClient adaptorClient,
                                          ServiceRegistryClient serviceRegistryClient){
        this.adaptorSessionRepository = adaptorSessionRepository;
        this.adaptorClient = adaptorClient;
        this.serviceRegistryClient = serviceRegistryClient;
    }

    /**
     * Fetches the joint positions from all open adaptor sessions.
     * Persists the positions in the database, see {@link JointPositions}
     */
    @Scheduled(fixedRate = 30000)
    public void fetchJointPositionsForOpenAdaptorSessions(){
        log.info("Fetching joint positions for open adaptor sessions..");
        List<ServiceRegistrationConfigDTO> adaptorConfigs = serviceRegistryClient.getRegisteredAdaptors();
        List<AdaptorSession> openSessions = adaptorSessionRepository.findByState(SessionState.OPEN);
        Map<AdaptorSession, ServiceRegistrationConfigDTO> sessionConfigMap = new HashMap<>();

        for (AdaptorSession session : openSessions) {
            ServiceRegistrationConfigDTO matchingConfig = findMatchingConfig(session, adaptorConfigs);
            if (matchingConfig != null) {
                sessionConfigMap.put(session, matchingConfig);
            }
        }

        sessionConfigMap.forEach(this::fetchJointPosition);
        log.debug("Finished fetching joint positions for open adaptor sessions");
    }
    // private region methods
    private ServiceRegistrationConfigDTO findMatchingConfig(AdaptorSession session, List<ServiceRegistrationConfigDTO> adaptorConfigs) {
        String adaptorName = session.getAdaptorName();

        for (ServiceRegistrationConfigDTO config : adaptorConfigs) {
            if (adaptorName.equals(config.getName())) {
                return config;
            }
        }

        log.warn("No matching adaptor config found for session {} with adaptor name {}", session.getId(), adaptorName);
        return null;
    }

    private void fetchJointPosition(AdaptorSession session, ServiceRegistrationConfigDTO config) {
        log.info("Fetching joint positions for session {} with key {} from adaptor {}",
                session.getId(), session.getSessionKey(), config.getName());
        try {
            adaptorClient.getAttributeValue(session.getSessionKey(), AttributeKey.JOINT_POSITIONS, config)
                    .ifPresent(attributeValue -> {
                        if(attributeValue instanceof AttributeValue.JointPositions jointPositions){
                            session.addJointPositions(fromDTO(jointPositions));
                            adaptorSessionRepository.save(session);
                        }else {
                            log.warn("Returned attribute value is not of type JointPositions but of type {}: {}", attributeValue.getClass().getSimpleName(), attributeValue);
                        }
                    });
        } catch (SessionNotValidException e) {
            log.warn("Session {} of {} is not valid anymore. Ignoring it until official request from user.", session.getId(), config.getName());
        }
    }

    private JointPositions fromDTO(AttributeValue.JointPositions jointPositions) {
        return JointPositions.builder()
                .axis1(jointPositions.jointPositions().get(0))
                .axis2(jointPositions.jointPositions().get(1))
                .axis3(jointPositions.jointPositions().get(2))
                .axis4(jointPositions.jointPositions().get(3))
                .axis5(jointPositions.jointPositions().get(4))
                .axis6(jointPositions.jointPositions().get(5))
                .build();
    }
}
