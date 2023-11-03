package at.jku.swe.simcomp.webotsadaptor.rest;


import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.service.WebotsSimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class WebotsSimulationResourceController {
    private final WebotsSimulationService simulationService;
    public WebotsSimulationResourceController(WebotsSimulationService demoSimulationService) {
        this.simulationService = demoSimulationService;
    }

    @PostMapping
    public ResponseEntity<Void> addSimulation(@RequestBody WebotsSimulationConfig config) {
        simulationService.addSimulation(config);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeSimulation(@RequestBody WebotsSimulationConfig config) {
        simulationService.removeSimulation(config);
        return ResponseEntity.ok().build();
    }
}
