package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ServiceRegistryClient {
    private final RestTemplate restTemplate = new RestTemplate();
    public ServiceRegistryClient() {
    }

    public List<ServiceRegistrationConfigDTO> getRegisteredAdaptors() {
        ServiceRegistrationConfigDTO[] configs = restTemplate.getForObject(System.getenv("SERVICE_REGISTRY_ENDPOINT"),
                ServiceRegistrationConfigDTO[].class);
        return Arrays.asList(configs);
    }
}
