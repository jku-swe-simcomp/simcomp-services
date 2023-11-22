package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceService;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationRemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DemoSimulationService implements SimulationInstanceService {
    private static final Set<DemoSimulationRemovalListener> simulationRemovalListeners = Collections.synchronizedSet(new HashSet<>());
    public static final Set<SimulationInstanceConfig> simulations = Collections.synchronizedSet(new HashSet<>());

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

    public void addSimulationRemovalListener(DemoSimulationRemovalListener listener) {
        simulationRemovalListeners.add(listener);
        log.info("Added simulation removal listener: {}", listener);
    }

    public void notifySimulationRemovalListeners(SimulationInstanceConfig config) {
        simulationRemovalListeners.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation removal listeners about removal of: {}", config);
    }
}
