package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.InvalidCommandParametersException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommandVisitor;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import at.jku.swe.simcomp.commons.ErrorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.util.List;

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
        ExecutionResultDTO executionResultDTO = executionCommand.accept(executionCommandVisitor, sessionId);
        return ResponseEntity.ok(executionResultDTO);
    }

    /**
     * Endpoint to execute a sequence of actions.
     * @return a response entity with details about the execution
     */
    @Override
    @PostMapping("/{sessionId}/sequence/execute")
    public final ResponseEntity<ExecutionResultDTO> executeSequence(@RequestBody List<ExecutionCommand> executionCommands, @PathVariable String sessionId) throws Exception {
        ExecutionResultDTO executionResultDTO = executionCommandVisitor.visitMultiple(executionCommands, sessionId);
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

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleSessionNotValidException(SessionNotValidException e){
        ErrorDTO result = ErrorDTO.builder()
                .code(400)
                .message("Session not valid with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(400).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleInvalidCommandParametersException(InvalidCommandParametersException e){
        ErrorDTO result = ErrorDTO.builder()
                .code(400)
                .message("Invalid command parameters with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(400).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleSessionInitializationFailedException(SessionInitializationFailedException e){
        ErrorDTO result = ErrorDTO.builder()
                .code(500)
                .message("Session initialization failed with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(500).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleServiceRegistrationFailedException(ServiceRegistrationFailedException e){
       ErrorDTO result = ErrorDTO.builder()
               .code(500)
               .message("Could not (un-)register at service registry with message: %s".formatted(e.getMessage()))
               .build();
       return ResponseEntity.status(500).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUnsupportedOperation(UnsupportedOperationException e){
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(404).body(errorDTO);
    }
    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleJsonProcessingException(JsonProcessingException e){
        ErrorDTO result = ErrorDTO.builder()
                .code(500)
                .message("Error while processing JSON with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(500).body(result);
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
}
