package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import at.jku.swe.simcomp.commons.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;

/**
 * Abstract base class for endpoints acting as adaptors for simulations.
 * Offers functionality for registration at the service-registry.
 * For registration, an environment variable SERVICE_REGISTRY_ENDPOINT has to point to the endpoint of the
 * service-registry.
 */
public abstract class AbstractAdaptorEndpoint {
    /**
     * Indicates if auto-registration is enabled (if no value is passed to the constructor explicitly).
     */
    private static final boolean IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT = true;
    /**
     * The registry-client used for registering/unregistering the endpoint.
     */
    protected final ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient();
    /**
     * The configuration which is used to register the endpoint.
     */
    protected final ServiceRegistrationConfigDTO serviceRegistrationConfigDTO;
    /**
     * The service
     */
    protected final AdaptorEndpointService adaptorEndpointService;

    /**
     * Constructor.
     * Is equivalent to calling {@link AbstractAdaptorEndpoint} with {@link #IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT}
     * @param serviceRegistrationConfigDTO the config
     * @param adaptorEndpointService the service
     */
    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                      AdaptorEndpointService adaptorEndpointService){
        this(serviceRegistrationConfigDTO, adaptorEndpointService, IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT);
    }

    /**
     * Constructor.
     * Automatically registers the adaptor if auto-registration is enabled.
     * @param serviceRegistrationConfigDTO the config
     * @param adaptorEndpointService the service
     * @param isAutoRegistrationEnabled flag indicating if registration should be attempted automatically
     *                                  in the course of the object initialization.
     */
    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                      AdaptorEndpointService adaptorEndpointService,
                                      boolean isAutoRegistrationEnabled){
        this.serviceRegistrationConfigDTO=serviceRegistrationConfigDTO;
        this.adaptorEndpointService=adaptorEndpointService;
        if(isAutoRegistrationEnabled){
           registerThisAdaptorEndpointAtServiceRegistry();
        }
    }

    /**
     * Endpoint to execute an action.
     * @param executionCommandDTO the command to execute
     * @return a response entity with details about the execution
     */
    @PostMapping("/action/execute")
    public final ResponseEntity<ExecutionResultDTO> executeAction(@RequestBody ExecutionCommandDTO executionCommandDTO){
        if(!this.serviceRegistrationConfigDTO.getSupportedActions().contains(executionCommandDTO.getActionType())){
           throw new UnsupportedOperationException("The action %s is not supported by this adaptor");
        }
        ExecutionResultDTO executionResultDTO = adaptorEndpointService.executeAction(executionCommandDTO);
        return ResponseEntity.ok(executionResultDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDTO> handleUnsupportedOperation(UnsupportedOperationException e){
        ErrorDTO errorDTO = ErrorDTO.builder()
                .code(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(404).body(errorDTO);
    }

    /**
     * Abstract endpoint to get an attribute.
     * @return the attribute
     */
    @GetMapping("/attribute/{name}")
    public final ResponseEntity<String> getAttribute(@PathVariable String name){
        String attributeValue = adaptorEndpointService.getAttributeValue(name);
        return ResponseEntity.ok(attributeValue);
    }

    /**
     * Endpoint for health check.
     * @return the attribute
     */
    @GetMapping("/health")
    public final ResponseEntity<Void> healthCheck(){
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
            System.out.println("Could not unregister from service with message: %s".formatted(e.getMessage()));
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
