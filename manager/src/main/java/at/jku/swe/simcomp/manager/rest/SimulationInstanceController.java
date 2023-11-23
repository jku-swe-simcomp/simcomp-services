package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.manager.dto.AvailableServicesDTO;
import at.jku.swe.simcomp.manager.service.SimulationInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/simulation")
@Slf4j
public class SimulationInstanceController {
    private final SimulationInstanceService service;

    public SimulationInstanceController(SimulationInstanceService service){
        this.service = service;
    }

    @GetMapping("/type")
    public ResponseEntity<AvailableServicesDTO> getAvailableSimulations(){
        log.info("Request to return available simulations received.");
        var simulations = service.getAvailableSimulations();
        log.info("Available simulations: {}", simulations);
        return ResponseEntity.ok(new AvailableServicesDTO(simulations));
    }

    @GetMapping("/instance")
    public ResponseEntity<List<SimulationInstanceConfig>> getSimulationInstances(){
        log.info("Request to return available simulation instances received.");
        return ResponseEntity.ok(service.getSimulationInstances());
    }

    @PostMapping("/instance")
    public ResponseEntity<Void> registerSimulationInstanceForAdaptor(@RequestBody SimulationInstanceConfig config) throws Exception {
        log.info("Request to register simulation instance {} for simulation {} received.", config);
        service.registerSimulationInstanceForAdaptor(config);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}/instance")
    public ResponseEntity<List<SimulationInstanceConfig>> getSimulationInstances(@PathVariable String name){
        log.info("Request to return available simulation instances of simulation {} received.", name);
        return ResponseEntity.ok(service.getSimulationInstances(name));
    }

    @DeleteMapping("/{simulationName}/instance/{instanceId}")
    public ResponseEntity<Void> deleteSimulationInstance(@PathVariable String simulationName, @PathVariable String instanceId){
        log.info("Request to delete simulation instance {} of simulation {} received.", instanceId, simulationName);
        service.deleteSimulationInstance(simulationName, instanceId);
        return ResponseEntity.ok().build();
    }
}
