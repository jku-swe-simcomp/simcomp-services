package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ServiceRegistryClient {
    private final RestTemplate restTemplate;
    private final Boolean isInverseKinematicsEnabled;
    public ServiceRegistryClient(RestTemplate restTemplate,
                                 @Value("${application.kinematics.inverse.enabled}") Boolean isInverseKinematicsEnabled) {
        this.restTemplate = restTemplate;
        this.isInverseKinematicsEnabled = Objects.requireNonNullElse(isInverseKinematicsEnabled, false);
    }

    public List<ServiceRegistrationConfigDTO> getRegisteredAdaptors() {
        log.debug("Fetching available adaptors...");
        // Note: default-object for endpoint does not make sense, but is required for the unit-tests
        ServiceRegistrationConfigDTO[] configs = restTemplate.getForObject(Objects.requireNonNullElse(System.getenv("SERVICE_REGISTRY_ENDPOINT"), ""),
                ServiceRegistrationConfigDTO[].class);
        log.debug("Available adaptor configurations: {}", configs);
        List<ServiceRegistrationConfigDTO> configList = Arrays.asList(configs);
        if(isInverseKinematicsEnabled){
            log.info("Adding Pose as supported actions as inverse kinematics is enabled");
            configList.forEach(c -> c.getSupportedActions().add(ActionType.POSE));
        }
        return configList;
    }
}
