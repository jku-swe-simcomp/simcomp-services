package at.jku.swe.simcomp.demoadaptor.rest;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AbstractAdaptorEndpoint;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that implements {@link AbstractAdaptorEndpoint} which offers automatic registration
 * at the service-registry.
 * A configuration bean {@link ServiceRegistrationConfigDTO} has to be injected into the constructor of this class,
 * and passed to the super()-constructor.
 * This bean should contain all available endpoints as {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointDeclarationDTO},
 * that should be registered at the registry.
 * The endpoint declaration contains only the type {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType},
 * and the path of the endpoint, but the request-methods have to match as well.
 * See the available endpoint types for further information.
 *
 * Note: this is a demo endpoint with no real functionality.
 */
@RestController
@RequestMapping("/api")
public class AdaptorEndpointController extends AbstractAdaptorEndpoint {
    /**
     * Constructor
     * @param serviceRegistrationConfigDTO the config used to register the adaptor
     * @param isAutoRegistrationEnabled boolean indicating if registration should be performed automatically at initialization.
     *                                  If disabled or in case of errors, one could implement an endpoint for triggering
     *                                  registration manually using the {@link #serviceRegistryClient}.
     */
    protected AdaptorEndpointController(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                        @Value("${adaptor.endpoint.autoregistration.enabled}") Boolean isAutoRegistrationEnabled) {
        super(serviceRegistrationConfigDTO, isAutoRegistrationEnabled);
    }

    /**
     * The endpoint implementing the type {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType#EXECUTE_ACTION}.
     * Endpoint has to be configured in the {@link #serviceRegistrationConfigDTO}
     * @param executionCommandDTO the command to execute
     * @return a {@link ResponseEntity} containing details about the execution
     */
    @Override
    @PostMapping("/execute")
    public ResponseEntity<ExecutionResponseDTO> executeAction(@RequestBody ExecutionCommandDTO executionCommandDTO) {
        return ResponseEntity.ok().build();
    }

    /**
     * The endpoint implementing the type {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType#GET_ATTRIBUTE}.
     * Endpoint has to be configured in the {@link #serviceRegistrationConfigDTO}
     * @return the attribute value
     */
    @Override
    @GetMapping("/attribute")
    public ResponseEntity<String> getAttribute() {
        return ResponseEntity.ok("Am healthy!");
    }

    /**
     * The endpoint implementing the type {@link at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType#HEALTH_CHECK}.
     * Endpoint has to be configured in the {@link #serviceRegistrationConfigDTO}
     * @return a response entity indicating healthiness of the endpoint
     */
    @Override
    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
