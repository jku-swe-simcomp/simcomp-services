package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ServiceRegistryClient {
    private final RestTemplate restTemplate;
    public ServiceRegistryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ServiceRegistrationConfigDTO> getRegisteredAdaptors() {
        log.debug("Fetching available adaptors...");
        // Note: default-object for endpoint does not make sense, but is required for the unit-tests
        ServiceRegistrationConfigDTO[] configs = restTemplate.getForObject(Objects.requireNonNullElse(System.getenv("SERVICE_REGISTRY_ENDPOINT"), ""),
                ServiceRegistrationConfigDTO[].class);
        log.debug("Available adaptor configurations: {}", configs);
        return Arrays.asList(configs);
    }
}
