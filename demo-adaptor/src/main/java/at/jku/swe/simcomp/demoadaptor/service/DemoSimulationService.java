package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationConfig;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationRemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DemoSimulationService {
    private static final Set<DemoSimulationRemovalListener> simulationRemovalListeners = Collections.synchronizedSet(new HashSet<>());
    public static final Set<DemoSimulationConfig> simulations = Collections.synchronizedSet(new HashSet<>());

    public void addSimulation(DemoSimulationConfig config) {
        simulations.add(config);
        log.info("Added simulation: {}", config);
    }

    public void removeSimulation(DemoSimulationConfig config) {
        if(simulations.contains(config)){
            simulations.remove(config);
            notifySimulationRemovalListeners(config);
            log.info("Removed simulation: {}", config);
        }
        log.info("Simulation {} not found", config);
    }

    public void addSimulationRemovalListener(DemoSimulationRemovalListener listener) {
        simulationRemovalListeners.add(listener);
        log.info("Added simulation removal listener: {}", listener);
    }

    public void notifySimulationRemovalListeners(DemoSimulationConfig config) {
        simulationRemovalListeners.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation removal listeners about removal of: {}", config);
    }
}
