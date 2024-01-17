package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * This class contains the endpoints for the simulation instance management.
 */
@RestController
@RequestMapping("/simulation/instance")
public class SimulationInstanceController {
    private final SimulationInstanceService simulationService;

    /**
     * Constructor
     * @param demoSimulationService The service for the simulation instance management.
     */
    public SimulationInstanceController(SimulationInstanceService demoSimulationService) {
        this.simulationService = demoSimulationService;
    }

    /**
     * This method adds a new simulation instance.
     * @param config The configuration of the simulation instance.
     * @return The response entity.
     * @throws Exception If the simulation instance could not be added.
     */
    @PostMapping
    public ResponseEntity<Void> addSimulationInstance(@RequestBody SimulationInstanceConfig config) throws Exception {
        simulationService.addSimulationInstance(config);
        return ResponseEntity.ok().build();
    }

    /**
     * This method removes a simulation instance.
     * @param instanceId The id of the simulation instance.
     * @return The response entity.
     * @throws Exception If the simulation instance could not be removed.
     */
    @DeleteMapping("/{instanceId}")
    public ResponseEntity<Void> removeSimulationInstance(@PathVariable String instanceId) throws Exception {
        simulationService.removeSimulationInstance(instanceId);
        return ResponseEntity.ok().build();
    }

    /**
     * This method returns all simulation instances.
     * @return The response entity.
     * @throws Exception If the simulation instances could not be fetched.
     */
    @GetMapping
    public ResponseEntity<Set<SimulationInstanceConfig>> getSimulationInstance() throws Exception {
        return ResponseEntity.ok().body(simulationService.getSimulationInstances());
    }
}
