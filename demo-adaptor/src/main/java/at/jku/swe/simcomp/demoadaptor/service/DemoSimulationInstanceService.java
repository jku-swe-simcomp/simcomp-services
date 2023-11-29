package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceService;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationRemovalListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DemoSimulationInstanceService implements SimulationInstanceService {
    private static final Set<DemoSimulationRemovalListener> simulationInstanceRemovalListeners = Collections.synchronizedSet(new HashSet<>());
    public static final Set<SimulationInstanceConfig> instances = Collections.synchronizedSet(new HashSet<>());

    private final String adaptorName;
    public DemoSimulationInstanceService(@Value("${adaptor.endpoint.name}") String adaptorName) {
        this.adaptorName = adaptorName;
    }

    @Override
    public void addSimulationInstance(SimulationInstanceConfig config) throws BadRequestException {
        if(!config.getSimulationName().equals(this.adaptorName)){
            throw new BadRequestException("Simulation name does not match the name of this adaptor");
        }
        if(instances.stream().anyMatch(instance -> instance.getInstanceHost().equals(config.getInstanceHost()) &&
                instance.getInstancePort().equals(config.getInstancePort()))){
            throw new BadRequestException("Simulation instance with same host and port already exists");
        }
        instances.add(config);
        log.info("Added simulation: {}", config);
    }

    @Override
    public void removeSimulationInstance(String instanceId) {
        Optional<SimulationInstanceConfig> config = instances.stream().filter(simulation -> simulation.getInstanceId().equals(instanceId)).findFirst();
        if(config.isPresent()){
            instances.remove(config.get());
            notifySimulationRemovalListeners(config.get());
            log.info("Removed simulation: {}", config.get());
        }else {
            log.info("Simulation instance {} not found", instanceId);
        }
    }

    @Override
    public Set<SimulationInstanceConfig> getSimulationInstances() throws Exception {
        return instances;
    }

    public void addSimulationRemovalListener(DemoSimulationRemovalListener listener) {
        simulationInstanceRemovalListeners.add(listener);
        log.info("Added simulation removal listener: {}", listener);
    }

    public void notifySimulationRemovalListeners(SimulationInstanceConfig config) {
        simulationInstanceRemovalListeners.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation instance removal listeners about removal of: {}", config);
    }
}
