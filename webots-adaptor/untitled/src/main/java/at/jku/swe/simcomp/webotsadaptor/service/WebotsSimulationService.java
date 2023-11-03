package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationRemovalListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class WebotsSimulationService {
    private static final Set<WebotsSimulationRemovalListener> simulationRemovalListeners = Collections.synchronizedSet(new HashSet<>());
    public static final Set<WebotsSimulationConfig> simulations = Collections.synchronizedSet(new HashSet<>());

    private static final Logger log = LogManager.getLogger();

    public void addSimulation(WebotsSimulationConfig config) {
        simulations.add(config);
        log.info("Added simulation: {}", config);
    }

    public void removeSimulation(WebotsSimulationConfig config) {
        if(simulations.contains(config)){
            simulations.remove(config);
            notifySimulationRemovalListeners(config);
            log.info("Removed simulation: {}", config);
        }
        log.info("Simulation {} not found", config);
    }

    public void addSimulationRemovalListener(WebotsSimulationRemovalListener listener) {
        simulationRemovalListeners.add(listener);
        log.info("Added simulation removal listener: {}", listener);
    }

    public void notifySimulationRemovalListeners(WebotsSimulationConfig config) {
        simulationRemovalListeners.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation removal listeners about removal of: {}", config);
    }
}
