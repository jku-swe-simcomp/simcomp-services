package at.jku.swe.simcomp.demoadaptor.rest;

import at.jku.swe.simcomp.commons.adaptor.endpoint.AbstractAdaptorEndpoint;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.demoadaptor.service.DemoAdaptorEndpointService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that implements {@link AbstractAdaptorEndpoint} which offers automatic registration
 * at the service-registry.
 * A configuration bean {@link ServiceRegistrationConfigDTO} has to be injected into the constructor of this class,
 * and passed to the super()-constructor.
 * The config should contain all supported {@link at.jku.swe.simcomp.commons.adaptor.dto.ActionType} by the
 * implemented {@link at.jku.swe.simcomp.commons.adaptor.endpoint.AdaptorEndpointService}.
 * Note: this is a demo endpoint with no real functionality.
 */
@RestController
@RequestMapping("/api")
public class DemoAdaptorEndpointController extends AbstractAdaptorEndpoint {
    /**
     * Constructor
     * @param serviceRegistrationConfigDTO the config used to register the adaptor
     * @param isAutoRegistrationEnabled boolean indicating if registration should be performed automatically at initialization.
     *                                  If disabled or in case of errors, one could implement an endpoint for triggering
     *                                  registration manually using the {@link #serviceRegistryClient}.
     */
    protected DemoAdaptorEndpointController(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                            DemoAdaptorEndpointService demoAdaptorEndpointService,
                                            @Value("${adaptor.endpoint.autoregistration.enabled}") Boolean isAutoRegistrationEnabled) {
        super(serviceRegistrationConfigDTO, demoAdaptorEndpointService, isAutoRegistrationEnabled);
    }
}
