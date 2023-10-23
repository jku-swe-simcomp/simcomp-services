package at.jku.swe.simcomp.demoadaptor.rest;

import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationConfig;
import at.jku.swe.simcomp.demoadaptor.service.DemoSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/simulation")
public class DemoSimulationResourceController {
    private final DemoSimulationService simulationService;
    public DemoSimulationResourceController(DemoSimulationService demoSimulationService) {
        this.simulationService = demoSimulationService;
    }

    @PostMapping
    public ResponseEntity<Void> addSimulation(@RequestBody DemoSimulationConfig config) {
        simulationService.addSimulation(config);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSimulation(@RequestBody DemoSimulationConfig config) {
        simulationService.removeSimulation(config);
        return ResponseEntity.ok().build();
    }
}
