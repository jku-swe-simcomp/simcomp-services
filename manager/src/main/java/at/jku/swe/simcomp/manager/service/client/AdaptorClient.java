package at.jku.swe.simcomp.manager.service.client;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.rest.exception.CommandExecutionFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import static at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointConstants.*;

/**
 * This service is responsible for communicating with the adaptors.
 */
@Service
@Slf4j
public class AdaptorClient {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Constructor
     * @param restTemplate the rest template
     * @param objectMapper the object mapper
     */
    public AdaptorClient(@Qualifier("noExceptionThrowingRestTemplate") RestTemplate restTemplate,
                         ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Tries to initialize a session with the given adaptor.
     * @param adaptorConfig the adaptor config
     * @return the session key if successful, empty otherwise
     */
    public Optional<String> getSession(@NonNull ServiceRegistrationConfigDTO adaptorConfig) {
        String url = getDomain(adaptorConfig) + INIT_SESSION_PATH;
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(null), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Obtained adaptor session of {} with key {}", adaptorConfig.getName(), response.getBody());
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

    /**
     * Tries to initialize a session with the given adaptor for a given simulation instance.
     * @param adaptorConfig the adaptor config
     * @param instanceId the simulation instance id
     * @return the session key if successful, empty otherwise
     */
    public Optional<String> getSession(@NonNull ServiceRegistrationConfigDTO adaptorConfig,
                                       @NonNull String instanceId) {
        String url = getDomain(adaptorConfig) + getInitSessionPathWithInstanceId(instanceId);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(null), String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Obtained adaptor session for simulation {}, instance {} with key {}", adaptorConfig.getName(), instanceId, response.getBody());
                return Optional.ofNullable(response.getBody());
            } else {
                log.info("Non-200 response when trying to obtain instance {} of {} session: {}", instanceId, adaptorConfig.getName(), response.getBody());
                return Optional.empty();
            }
        } catch (Exception e) {
            log.error("Error during REST call to initialize adaptor session for instance {} of {}. {}", instanceId, adaptorConfig.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Closes a session with the given adaptor.
     * @param adaptorConfig the adaptor config identifying the adaptor
     * @param sessionKey the key of the session to be closed
     */
    public void closeSession(@NonNull ServiceRegistrationConfigDTO adaptorConfig,
                             @NonNull String sessionKey){
        String url = getDomain(adaptorConfig) + getCloseSessionPathForSessionId(sessionKey);
        try {
            restTemplate.delete(url);
        } catch (Exception e) {
            log.error("Error during REST call to delete adaptor session. {}", e.getMessage());
        }
    }

    /**
     * Executes a command for a given adaptor session.
     * @param adaptorConfig the adaptor config identifying the adaptor
     * @param command the command to be executed
     * @param adaptorSessionKey the session key of the adaptor session
     * @return the execution result
     * @throws CommandExecutionFailedException if the command execution failed
     */
    public ExecutionResultDTO executeCommand(@NonNull ServiceRegistrationConfigDTO adaptorConfig,
                                             @NonNull ExecutionCommand command,
                                             @NonNull String adaptorSessionKey) throws CommandExecutionFailedException {
        String url = getDomain(adaptorConfig) + getExecuteActionPathForSessionId(adaptorSessionKey);
        ResponseEntity<String> response;
        try{
            response = restTemplate.postForEntity(url, new HttpEntity<>(command, null), String.class);
        }catch (Exception e){
            throw new CommandExecutionFailedException("Could not execute command %s for adaptor %s: %s".formatted(command, adaptorConfig.getName(), e.getMessage()), 500);
        }

        try{
            if (response.getStatusCode() == HttpStatus.OK) {
                log.info("Executed command for adaptor {}: {}", adaptorConfig.getName(), response.getBody());
                return objectMapper.readValue(response.getBody(), ExecutionResultDTO.class);
            } else {
                log.info("Non-200 response when trying to execute command for adaptor {}: {}", adaptorConfig.getName(), response.getBody());
                HttpErrorDTO error = objectMapper.readValue(response.getBody(), HttpErrorDTO.class);
                if(error == null){
                    throw new CommandExecutionFailedException("Could not deserialize response %s from %s".formatted(response.getBody(), adaptorConfig.getName()), 500);
                }
                throw new CommandExecutionFailedException(error.getMessage(), (int) error.getStatus());
            }
        }catch (JsonProcessingException e){
            throw new CommandExecutionFailedException("Could not deserialize response %s from %s".formatted(response.getBody(), adaptorConfig.getName()), 500);
        }
    }

    /**
     * Fetches the attribute-value from an adaptor for a given session
     * @param config the adaptor config identifying the adaptor
     * @param attributeKey the attribute key
     * @param sessionId the session id
     * @return the attribute value if successful, empty otherwise
     */
    public Optional<AttributeValue> getAttributeValue(@NonNull String sessionId,
                                                      @NonNull AttributeKey attributeKey,
                                                      @NonNull ServiceRegistrationConfigDTO config) throws SessionNotValidException {
        String url = getDomain(config) + getGetAttributePathForAttributeName(sessionId, attributeKey);
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity = restTemplate.getForEntity(url, String.class);
        }catch (Exception e){
            log.warn("Could not obtain attribute value for adaptor session {} of {}: {}", sessionId, config.getName(), e.getMessage());
            return Optional.empty();
        }

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.debug("Obtained attribute value for adaptor session {} of {} with key {}: {}", sessionId, config.getName(), attributeKey, responseEntity.getBody());
            try {
                return Optional.ofNullable(objectMapper.readValue(responseEntity.getBody(), AttributeValue.class));
            } catch (JsonProcessingException e) {
                log.warn("Could not deserialize response {} from {}", responseEntity.getBody(), config.getName());
            }
        }else{
            log.warn("Non-2xx response when trying to obtain attribute value for adaptor session {} of {}: {}", sessionId, config.getName(), responseEntity.getBody());
            if(responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED){
                throw new SessionNotValidException("Session %s for adaptor %s is not valid".formatted(sessionId, config.getName()));
            }
        }
        return Optional.empty();
    }

    /**
     * Registers a simulation instance for a given adaptor.
     * @param serviceRegistrationConfigDTO the adaptor config
     * @param config the simulation instance config
     * @throws Exception if registration fails
     */
    public void registerSimulationInstanceForAdaptor(@NonNull ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                                     @NonNull SimulationInstanceConfig config) throws Exception {
        String url = getDomain(serviceRegistrationConfigDTO) + SIMULATION_INSTANCE_PATH;
        HttpEntity<SimulationInstanceConfig> requestEntity = new HttpEntity<>(config, null);
        var response = restTemplate.postForEntity(url, requestEntity, Void.class);
        if(!response.getStatusCode().is2xxSuccessful()){
            throw new Exception("Could not register simulation instance %s for adaptor %s with message %s".formatted(config, serviceRegistrationConfigDTO.getName(), response.getBody()));
        }
    }

    /**
     * Returns all simulation instances for a given adaptor.
     * @param config the adaptor config identifying the adaptor
     * @return the simulation instances
     */
    public List<SimulationInstanceConfig> getSimulationInstances(ServiceRegistrationConfigDTO config) {
        String url = getDomain(config) + SIMULATION_INSTANCE_PATH;
        ResponseEntity<List<SimulationInstanceConfig>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return responseEntity.getBody();
    }

    /**
     * Deletes a simulation instance for a given adaptor.
     * @param serviceRegistrationConfigDTO the adaptor config identifying the adaptor
     * @param instanceId the simulation instance id identifying the simulation instance to be deleted
     */
    public void deleteSimulationInstance(@NonNull ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                         @NonNull String instanceId){
        String url = getDomain(serviceRegistrationConfigDTO)+ getDeleteSimulationInstancePathForInstanceId(instanceId);
        restTemplate.delete(url);
    }
}
