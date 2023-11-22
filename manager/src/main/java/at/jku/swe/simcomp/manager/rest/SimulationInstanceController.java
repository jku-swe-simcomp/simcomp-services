package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.manager.dto.AvailableServicesDTO;
import at.jku.swe.simcomp.manager.service.SimulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulation")
@Slf4j
public class SimulationInstanceController {
    private final SimulationService service;

    public SimulationInstanceController(SimulationService service){
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
    public ResponseEntity<Map<String, List<SimulationInstanceConfig>>> getSimulationInstances(){
        log.info("Request to return available simulation instances received.");
        return ResponseEntity.ok(service.getSimulationInstances());
    }


    @PostMapping("/{name}/instance")
    public ResponseEntity<Void> registerSimulationInstanceForAdaptor(@PathVariable String name, @RequestBody SimulationInstanceConfig config){
        log.info("Request to register simulation instance {} for adaptor {} received.", config, name);
        service.registerSimulationInstanceForAdaptor(name, config);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}/instance")
    public ResponseEntity<List<SimulationInstanceConfig>> getSimulationInstances(@PathVariable String name){
        log.info("Request to return available simulation instances for simulation {} received.", name);
        return ResponseEntity.ok(service.getSimulationInstances(name));
    }

    @DeleteMapping("/{name}/instance")
    public ResponseEntity<List<SimulationInstanceConfig>> deleteSimulationInstance(@PathVariable String name, @RequestBody SimulationInstanceConfig config){
        log.info("Request to delete simulation instance {} for simulation {} received.", config, name);
        service.deleteSimulationInstance(name, config);
        return ResponseEntity.ok().build();
    }
}
