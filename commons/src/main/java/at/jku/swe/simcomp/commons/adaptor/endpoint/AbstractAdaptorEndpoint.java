package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import org.springframework.http.ResponseEntity;

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
     * Constructor.
     * Is equivalent to calling {@link AbstractAdaptorEndpoint} with {@link #IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT}
     * @param serviceRegistrationConfigDTO the config
     */
    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO){
        this(serviceRegistrationConfigDTO, IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT);
    }

    /**
     * Constructor.
     * Automatically registers the adaptor if auto-registration is enabled.
     * @param serviceRegistrationConfigDTO the config
     * @param isAutoRegistrationEnabled flag indicating if registration should be attempted automatically
     *                                  in the course of the object initialization.
     */
    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO, boolean isAutoRegistrationEnabled){
        this.serviceRegistrationConfigDTO=serviceRegistrationConfigDTO;
        if(isAutoRegistrationEnabled){
           registerThisAdaptorEndpointAtServiceRegistry();
        }
    }

    /**
     * Abstract endpoint to execute an action.
     * Has to be exposed as REST endpoint that accepts POST requests.
     * Path has to be configured in the configuration bean {@link #serviceRegistrationConfigDTO},
     * for the {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType#EXECUTE_ACTION}
     * @param executionCommandDTO the command to execute
     * @return a response entity with details about the execution
     */
    public abstract ResponseEntity<ExecutionResponseDTO> executeAction(ExecutionCommandDTO executionCommandDTO);

    /**
     * Abstract endpoint to get an attribute.
     * Has to be exposed as REST endpoint that accepts GET requests.
     * Path has to be configured in the configuration bean {@link #serviceRegistrationConfigDTO},
     * for the {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType#GET_ATTRIBUTE}
     * @return the attribute
     */
    public abstract ResponseEntity<String> getAttribute();

    /**
     * Abstract endpoint to perform a health check.
     * Has to be exposed as REST endpoint that accepts GET requests.
     * Path has to be configured in the configuration bean {@link #serviceRegistrationConfigDTO},
     * for the {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType#HEALTH_CHECK}
     * @return response-entity indicating the health
     */
    public abstract ResponseEntity<Void> healthCheck();

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
