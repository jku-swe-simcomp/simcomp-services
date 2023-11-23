package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointConstants;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.rest.exception.CommandExecutionFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AdaptorClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    public AdaptorClient(RestTemplateBuilder restTemplateBuilder,
                         NoExceptionThrowingResponseErrorHandler noExceptionThrowingResponseErrorHandler) {
        restTemplate = restTemplateBuilder
                .errorHandler(noExceptionThrowingResponseErrorHandler)
                .build();
    }

    public Optional<String> getSession(ServiceRegistrationConfigDTO adaptorConfig) {
        String url = "http://" + adaptorConfig.getHost() + ":" + adaptorConfig.getPort() + AdaptorEndpointConstants.INIT_SESSION_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(null), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Obtained adaptor session for {} with key {}", adaptorConfig.getName(), response.getBody());
                return Optional.ofNullable(response.getBody());
            } else {
                log.info("Non-200 response when trying to obtain session for {}: {}", adaptorConfig.getName(), response.getBody());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error during REST call to initialize adaptor session. {}", e.getMessage());
            return Optional.empty();
        }
    }

    public void closeSession(ServiceRegistrationConfigDTO adaptorConfig, String sessionKey){
        String url = "http://" + adaptorConfig.getHost() + ":" + adaptorConfig.getPort() + AdaptorEndpointConstants.getCloseSessionPathForSessionId(sessionKey);
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error during REST call to delete adaptor session. {}", e.getMessage());
        }
    }

    public ExecutionResultDTO executeCommand(ServiceRegistrationConfigDTO adaptorConfig, ExecutionCommand command, String adaptorSessionKey) throws CommandExecutionFailedException {
        String url = "http://" + adaptorConfig.getHost() + ":" + adaptorConfig.getPort() + AdaptorEndpointConstants.getExecuteActionPathForSessionId(adaptorSessionKey);
        ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(command, null), String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("Executed command for adaptor {}: {}", adaptorConfig.getName(), response.getBody());
            try {
                return objectMapper.readValue(response.getBody(), ExecutionResultDTO.class);
            } catch (JsonProcessingException e) {
                throw new CommandExecutionFailedException("Could not deserialize response %s from %s".formatted(response.getBody(), adaptorConfig.getName()), 500);
            }
        } else {
            log.info("Non-200 response when trying to execute command for adaptor {}: {}", adaptorConfig.getName(), response.getBody());
            try {
                HttpErrorDTO error = objectMapper.readValue(response.getBody(), HttpErrorDTO.class);
                throw new CommandExecutionFailedException(error.getMessage(), (int) error.getStatus());
            } catch (JsonProcessingException e) {
                throw new CommandExecutionFailedException("Could not deserialize response %s from %s".formatted(response.getBody(), adaptorConfig.getName()), 500);
            }
        }
    }

    public Optional<AttributeValue> getAttributeValue(String sessionId, AttributeKey attributeKey, ServiceRegistrationConfigDTO config){
        String url = "http://" + config.getHost() + ":" + config.getPort() + AdaptorEndpointConstants.getGetAttributePathForAttributeName(sessionId, attributeKey);
        ResponseEntity<AttributeValue> responseEntity = restTemplate.getForEntity(url, AttributeValue.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.debug("Obtained attribute value for adaptor session {} of {} with key {}: {}", sessionId, config.getName(), attributeKey, responseEntity.getBody());
            return Optional.ofNullable(responseEntity.getBody());
        }else{
            log.warn("Non-2xx response when trying to obtain attribute value for adaptor session {} of {}: {}", sessionId, config.getName(), responseEntity.getBody());
            return Optional.empty();
        }
    }

    public void registerSimulationInstanceForAdaptor(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO, SimulationInstanceConfig config) throws Exception {
        String url = "http://" + serviceRegistrationConfigDTO.getHost() + ":" + serviceRegistrationConfigDTO.getPort() + AdaptorEndpointConstants.SIMULATION_INSTANCE_PATH;
        HttpEntity<SimulationInstanceConfig> requestEntity = new HttpEntity<>(config, null);
        var response = restTemplate.postForEntity(url, requestEntity, Void.class);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new Exception("Could not register simulation instance %s for adaptor %s with message %s".formatted(config, serviceRegistrationConfigDTO.getName(), response.getBody()));
        }
    }

    public List<SimulationInstanceConfig> getSimulationInstances(ServiceRegistrationConfigDTO config) {
        String url = "http://" + config.getHost() + ":" + config.getPort() + AdaptorEndpointConstants.SIMULATION_INSTANCE_PATH;
        ResponseEntity<List<SimulationInstanceConfig>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return responseEntity.getBody();
    }

    public void deleteSimulationInstance(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO, String instanceId){
        String url = "http://" + serviceRegistrationConfigDTO.getHost() + ":" + serviceRegistrationConfigDTO.getPort() + AdaptorEndpointConstants.getDeleteSimulationInstancePathForInstanceId(instanceId);
        restTemplate.delete(url);
    }
}
