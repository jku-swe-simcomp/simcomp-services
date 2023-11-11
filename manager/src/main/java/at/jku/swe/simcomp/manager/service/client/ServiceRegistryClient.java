package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ServiceRegistryClient {
    private final RestTemplate restTemplate = new RestTemplate();
    public ServiceRegistryClient() {
    }

    public List<ServiceRegistrationConfigDTO> getRegisteredAdaptors() {
        log.debug("Fetching available adaptors...");
        ServiceRegistrationConfigDTO[] configs = restTemplate.getForObject(System.getenv("SERVICE_REGISTRY_ENDPOINT"),
                ServiceRegistrationConfigDTO[].class);
        log.debug("Available adaptor configurations: {}", configs);
        return Arrays.asList(configs);
    }
}
