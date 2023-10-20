package at.jku.swe.simcomp.commons.registry;

import at.jku.swe.simcomp.commons.adaptor.endpoint.dto.AdaptorEndpointDeclarationDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.dto.AdaptorEndpointType;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.registry.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class ServiceRegistryClient {
    private static final String DELETE_PATH_SUFFIX = "/{name}";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

//    public static void main(String[] args) {
//        var client = new ServiceRegistryClient();
//        try {
//            client.unregister("DEMO");
//            client.register(ServiceRegistrationConfigDTO.builder()
//                    .name("DEMO")
//                    .host("")
//                    .port(8080)
//                    .adaptorEndpoints(List.of(AdaptorEndpointDeclarationDTO.builder()
//                            .endpointType(AdaptorEndpointType.EXECUTE_ACTION)
//                            .path("/test").build()))
//                    .build());
//        } catch (JsonProcessingException | ServiceRegistrationFailedException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void register(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO) throws JsonProcessingException, ServiceRegistrationFailedException {
        String serviceRegistryEndpointUrl = System.getenv("SERVICE_REGISTRY_ENDPOINT");

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
    public void unregister(String serviceName) throws ServiceRegistrationFailedException {
        String serviceRegistryEndpointUrl = System.getenv("SERVICE_REGISTRY_ENDPOINT") + DELETE_PATH_SUFFIX;
        try {
            restTemplate.delete(serviceRegistryEndpointUrl, serviceName);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new ServiceRegistrationFailedException("Could not unregister from service registry. Request returned %s".formatted(e.getMessage()));
        }

    }
}
