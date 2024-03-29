package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.CompositeCommandExecutionFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.RoboOperationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.CommandExecutionVisitor;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import org.json.simple.parser.ParseException;

/**
 * Class for endpoints acting as adaptors for simulations.
 * Offers functionality for registration at the service-registry.
 * And common endpoints.
 * For registration, an environment variable SERVICE_REGISTRY_ENDPOINT has to point to the endpoint of the
 * service-registry.
 */
@RestController
public class AdaptorEndpointController implements AdaptorEndpoint{
    /**
     * The registry-client used for registering/unregistering the endpoint.
     */
    private final ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient();
    /**
     * The configuration which is used to register the endpoint.
     */
    private final ServiceRegistrationConfigDTO serviceRegistrationConfigDTO;
    /**
     * The service
     */
    private final AdaptorEndpointService adaptorEndpointService;
    private final CommandExecutionVisitor commandExecutionVisitor;

    /**
     * Constructor.
     * Automatically registers the adaptor if auto-registration is enabled.
     * @param serviceRegistrationConfigDTO the config
     * @param adaptorEndpointService the service
     */
    public AdaptorEndpointController(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                     AdaptorEndpointService adaptorEndpointService,
                                     CommandExecutionVisitor commandExecutionVisitor){
        this.serviceRegistrationConfigDTO=serviceRegistrationConfigDTO;
        this.adaptorEndpointService=adaptorEndpointService;
        this.commandExecutionVisitor = commandExecutionVisitor;
        registerThisAdaptorEndpointAtServiceRegistry();
    }

    /**
     * Endpoint to initialize a session.
     * @param instanceId the id of the simulation instance requested by the user (optional)
     * @return a response entity with the id of the session
     * @throws SessionInitializationFailedException if the session could not be initialized
     */
    @Override
    @PostMapping("/session/init")
    public ResponseEntity<String> initSession(@RequestParam(required = false) String instanceId) throws SessionInitializationFailedException {
        if(instanceId == null){
            String sessionId = adaptorEndpointService.initSession();
            return ResponseEntity.ok(sessionId);
        }
        String sessionId = adaptorEndpointService.initSession(instanceId);
        return ResponseEntity.ok(sessionId);
    }

    /**
     * Endpoint to close a session.
     * @param sessionId the id of the session to close
     * @return a response entity
     * @throws SessionNotValidException if the session is not valid
     */
    @Override
    @DeleteMapping("/session/{sessionId}/close")
    public ResponseEntity<String> closeSession(@PathVariable String sessionId) throws SessionNotValidException {
        adaptorEndpointService.closeSession(sessionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to execute an action.
     * @param executionCommand the command to execute
     * @return a response entity with details about the execution
     */
    @Override
    @PostMapping("/{sessionId}/action/execute")
    public final ResponseEntity<ExecutionResultDTO> executeAction(@RequestBody ExecutionCommand executionCommand, @PathVariable String sessionId) throws Exception {
        ExecutionResultDTO executionResultDTO = null;
        try{
            executionResultDTO = executionCommand.accept(commandExecutionVisitor, sessionId);
        }catch (CompositeCommandExecutionFailedException e){
            if(e.getOriginalException() instanceof RoboOperationFailedException rofe){
                throw new RoboOperationFailedException(e.getMessageWithReports(), rofe.getState());
            }else {
                throw createExceptionWithMessage(e.getOriginalException().getClass(), e.getMessageWithReports());
            }
        }
        return ResponseEntity.ok(executionResultDTO);
    }

    /**
     * Abstract endpoint to get an attribute.
     * @return the attribute
     */
    @Override
    @GetMapping("/{sessionId}/attribute/{attribute}")
    public final ResponseEntity<AttributeValue> getAttribute(@PathVariable AttributeKey attribute, @PathVariable String sessionId) throws SessionNotValidException, RoboOperationFailedException, IOException, ParseException {
        AttributeValue value = adaptorEndpointService.getAttributeValue(attribute, sessionId);
        return ResponseEntity.ok(value);
    }

    /**
     * Abstract endpoint to get an attribute.
     * @return the attribute
     */
    @Override
    @GetMapping("/custom-commands")
    public final ResponseEntity<List<String>> getCustomCommandTypes() {
        List<String> value = adaptorEndpointService.getSupportedCustomCommandTypes();
        return ResponseEntity.ok(value);
    }

    /**
     * Abstract endpoint to get an attribute.
     * @return the attribute
     */
    @Override
    @GetMapping("/custom-commands/{type}/example")
    public final ResponseEntity<String> getCustomCommandTypeExampleJson(@PathVariable String type) {
        String value = adaptorEndpointService.getCustomCommandTypeExampleJson(type);
        return ResponseEntity.ok(value);
    }

    /**
     * Endpoint for health check.
     * @return the attribute
     */
    @Override
    @GetMapping("/health")
    public final ResponseEntity<Void> healthCheck(){
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for manually triggering registration at the service registry.
     * @return the attribute
     */
    @PostMapping("/registry/register")
    public final ResponseEntity<Void> registerAdaptor() throws ServiceRegistrationFailedException, JsonProcessingException {
        serviceRegistryClient.register(this.serviceRegistrationConfigDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for manually triggering unregistration from the service registry.
     * @return the attribute
     */
    @PostMapping("/registry/unregister")
    public final ResponseEntity<Void> unregisterAdaptor() throws ServiceRegistrationFailedException{
        serviceRegistryClient.unregister(this.serviceRegistrationConfigDTO.getName());
        return ResponseEntity.ok().build();
    }


    /**
     * Lifecycle method that gets called when object is destroyed.
     * Unregisters the endpoint according to the name configured in
     * {@link #serviceRegistrationConfigDTO}
     */
    @PreDestroy
    public void onDestroy(){
        try {
            serviceRegistryClient.unregister(this.serviceRegistrationConfigDTO.getName());
        } catch (ServiceRegistrationFailedException e) {
            // Note: Logging currently not working in commons so that it shows up in projects that depend on it
            System.out.printf("Could not unregister from service with message: %s%n", e.getMessage());
        }
    }

    /**
     * Utility method registering this adaptor asynchronously.
     * If the environment variable REGISTRATION_DELAY_MS is set, the thread will sleep for the specified amount of time.
     * This is useful if the service registry is not available yet.
     */
    private void registerThisAdaptorEndpointAtServiceRegistry() {
            new Thread(() -> {
                System.out.println("Registering adaptor at service registry...");
                String delay = System.getenv("REGISTRATION_DELAY_MS");
                if(delay != null){
                    System.out.printf("Delay configured. Waiting for %s ms before registration.%n", delay);
                    try {
                        Thread.sleep(Long.parseLong(delay));
                    } catch (InterruptedException e) {
                        System.out.println("Delay thread interrupted.");
                    }
                }
                try {
                    this.serviceRegistryClient.register(this.serviceRegistrationConfigDTO);
                    System.out.println("Registration completed.");
                } catch (ServiceRegistrationFailedException | JsonProcessingException e) {
                    System.out.println("Could not register adaptor endpoint at service registry.");
                }
            }).start();
    }

    /**
     * Utility method that returns an exception of type originalExClass with the specified message.
     * @param originalExClass the class of the exception to be created
     * @param message the message of the exception to be created
     * @return the exception
     * @param <T> the type of the exception to be created
     * @throws Exception if the original exception type doesn't have a constructor accepting a string
     */
    private <T extends Exception> T createExceptionWithMessage(Class<T> originalExClass, String message)
            throws Exception {
        try {
            Constructor<T> constructor = originalExClass.getConstructor(String.class);
            return constructor.newInstance(message);
        } catch (NoSuchMethodException e) {
            // The original exception type doesn't have a constructor accepting a string
            throw new Exception(message);
        }
    }
}
