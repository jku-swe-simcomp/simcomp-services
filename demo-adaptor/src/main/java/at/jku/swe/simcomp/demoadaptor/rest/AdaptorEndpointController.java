package at.jku.swe.simcomp.demoadaptor.rest;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.AbstractAdaptorEndpoint;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class AdaptorEndpointController extends AbstractAdaptorEndpoint {
    protected AdaptorEndpointController(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO,
                                        @Value("${adaptor.endpoint.autoregistration.enabled}") Boolean isAutoRegistrationEnabled) {
        super(serviceRegistrationConfigDTO, isAutoRegistrationEnabled);
    }

    @Override
    @PostMapping("/execute")
    public ResponseEntity<ExecutionResponseDTO> executeAction(@RequestBody ExecutionCommandDTO executionCommandDTO) {
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/attribute")
    public ResponseEntity<String> getAttribute() {
        return ResponseEntity.ok("Am healthy!");
    }

    @Override
    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
