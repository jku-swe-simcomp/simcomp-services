package at.jku.swe.simcomp.commons.adaptor.registration;

import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryConstants.*;

/**
 * Client to register an adaptor at the service-registry.
 * Environment variable SERVICE_REGISTRY_ENDPOINT has to be set.
 */
@NoArgsConstructor
public class ServiceRegistryClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Registers the adaptor by posting the passed config.
     * @param serviceRegistrationConfigDTO the config
     * @throws JsonProcessingException if the config cannot be serialized
     * @throws ServiceRegistrationFailedException if an exception occurs during registration
     */
    public void register(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO) throws JsonProcessingException, ServiceRegistrationFailedException {
        String serviceRegistryEndpointUrl = System.getenv(SERVICE_REGISTRY_ENDPOINT_ENVIRONMENT_VARIABLE_NAME);

        if(Objects.isNull(serviceRegistryEndpointUrl)){
            throw new ServiceRegistrationFailedException("Environment variable SERVICE_REGISTRY_ENDPOINT not set");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(serviceRegistrationConfigDTO), headers);
        try {
            restTemplate.postForEntity(serviceRegistryEndpointUrl, entity, JsonNode.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ServiceRegistrationFailedException("Could not register at service registry. Request returned %s".formatted(e.getMessage()));
        }
    }

    /**
     * Unregisters the adaptor by calling the endpoint with the name of the adaptor.
     * @param serviceName the name of the adaptor
     * @throws ServiceRegistrationFailedException if an exception occurs during unregistering.
     */
    public void unregister(String serviceName) throws ServiceRegistrationFailedException {
        String serviceRegistryEndpointUrl = System.getenv(SERVICE_REGISTRY_ENDPOINT_ENVIRONMENT_VARIABLE_NAME) + getUnregisterPathForAdaptorName(serviceName);
        try {
            restTemplate.delete(serviceRegistryEndpointUrl, serviceName);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ServiceRegistrationFailedException("Could not unregister from service registry. Request returned %s".formatted(e.getMessage()));
        }

    }
}
