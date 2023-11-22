package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceService;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.SimulationRemovalListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WebotsSimulationInstanceService implements SimulationInstanceService {
    private static final Set<SimulationRemovalListener> simulationRemovalListeners = Collections.synchronizedSet(new HashSet<>());
    public static final Set<SimulationInstanceConfig> simulations = Collections.synchronizedSet(new HashSet<>());

    private static final Logger log = LogManager.getLogger();

    @Override
    public void addSimulation(SimulationInstanceConfig config) {
        simulations.add(config);
        log.info("Added simulation: {}", config);
    }

    @Override
    public void removeSimulation(SimulationInstanceConfig config) {
        if(simulations.contains(config)){
            simulations.remove(config);
            notifySimulationRemovalListeners(config);
            log.info("Removed simulation: {}", config);
        }
        log.info("Simulation {} not found", config);
    }

    @Override
    public Set<SimulationInstanceConfig> getSimulationInstance() throws Exception {
        return simulations;
    }

    public void addSimulationRemovalListener(SimulationRemovalListener listener) {
        simulationRemovalListeners.add(listener);
        log.info("Added simulation removal listener: {}", listener);
    }

    public void notifySimulationRemovalListeners(SimulationInstanceConfig config) {
        simulationRemovalListeners.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation removal listeners about removal of: {}", config);
    }
}
