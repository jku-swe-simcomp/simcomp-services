package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.CompositeCommandExecutionFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommandVisitor;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.lang.reflect.Constructor;

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
    private final ExecutionCommandVisitor executionCommandVisitor;

    /**
     * Constructor.
     * Automatically registers the adaptor if auto-registration is enabled.
     * @param serviceRegistrationConfigDTO the config
     * @param adaptorEndpointService the service
     */
    public AdaptorEndpointController(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                     AdaptorEndpointService adaptorEndpointService,
                                     ExecutionCommandVisitor executionCommandVisitor){
        this.serviceRegistrationConfigDTO=serviceRegistrationConfigDTO;
        this.adaptorEndpointService=adaptorEndpointService;
        this.executionCommandVisitor=executionCommandVisitor;
        registerThisAdaptorEndpointAtServiceRegistry();
    }

    @Override
    @PostMapping("/session/init")
    public ResponseEntity<String> initSession() throws SessionInitializationFailedException {
        String sessionId = adaptorEndpointService.initSession();
        return ResponseEntity.ok(sessionId);
    }

    @Override
    @PostMapping("/session/{sessionId}/close")
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
            executionResultDTO = executionCommand.accept(executionCommandVisitor, sessionId);
        }catch (CompositeCommandExecutionFailedException e){
            throw createExceptionWithMessage(e.getOriginalException().getClass(), e.getMessage());
        }
        return ResponseEntity.ok(executionResultDTO);
    }
    /**
     * Abstract endpoint to get an attribute.
     * @return the attribute
     */
    @Override
    @GetMapping("/{sessionId}/attribute/{name}")
    public final ResponseEntity<String> getAttribute(@PathVariable String name, @PathVariable String sessionId) throws SessionNotValidException {
        String attributeValue = adaptorEndpointService.getAttributeValue(name, sessionId);
        return ResponseEntity.ok(attributeValue);
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
     * Endpoint for manually triggering registration.
     * @return the attribute
     */
    @PostMapping("/registry/register")
    public final ResponseEntity<Void> registerAdaptor() throws ServiceRegistrationFailedException, JsonProcessingException {
        serviceRegistryClient.register(this.serviceRegistrationConfigDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for manually triggering registration.
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
     * Utility method registering this adaptor.
     */
    private void registerThisAdaptorEndpointAtServiceRegistry() {
        try{
            this.serviceRegistryClient.register(this.serviceRegistrationConfigDTO);
        }catch(Exception e){//we want to catch all possible exceptions as this method is called during object initialization
            System.out.println("Could not register adaptor endpoint at service registry.");
        }
    }

    /**
     * Utility method that returns an exception of type originalExClass with the message of the wrapperException.
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
