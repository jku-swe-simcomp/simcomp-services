package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.manager.dto.AvailableServicesDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    private final ServiceRegistryClient client;

    public SimulationController(ServiceRegistryClient client){
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<AvailableServicesDTO> getAvailableSimulations(){
        var simulations =  client.getRegisteredAdaptors()
                .stream()
                .map(ServiceRegistrationConfigDTO::viewForDisplay)
                .toList();
        return ResponseEntity.ok(new AvailableServicesDTO(simulations));
    }
}
