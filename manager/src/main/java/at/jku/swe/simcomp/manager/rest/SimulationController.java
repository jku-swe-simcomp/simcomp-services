package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.manager.dto.AvailableServicesDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.manager.service.SimulationService;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
@Slf4j
public class SimulationController {
    private final SimulationService service;

    public SimulationController(SimulationService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<AvailableServicesDTO> getAvailableSimulations(){
        log.info("Request to return available simulations received.");
        var simulations = service.getAvailableSimulations();
        log.info("Available simulations: {}", simulations);
        return ResponseEntity.ok(new AvailableServicesDTO(simulations));
    }
}
