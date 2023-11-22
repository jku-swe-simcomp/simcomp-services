package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/simulation/instance")
public class SimulationInstanceController {
    private final SimulationInstanceService simulationService;
    public SimulationInstanceController(SimulationInstanceService demoSimulationService) {
        this.simulationService = demoSimulationService;
    }

    @PostMapping
    public ResponseEntity<Void> addSimulationInstance(@RequestBody SimulationInstanceConfig config) throws Exception {
        simulationService.addSimulation(config);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSimulationInstance(@RequestBody SimulationInstanceConfig config) throws Exception {
        simulationService.removeSimulation(config);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Set<SimulationInstanceConfig>> getSimulationInstance() throws Exception {
        return ResponseEntity.ok().body(simulationService.getSimulationInstance());
    }
}
