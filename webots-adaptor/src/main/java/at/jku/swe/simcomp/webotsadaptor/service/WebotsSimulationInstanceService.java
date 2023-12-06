package at.jku.swe.simcomp.webotsadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceService;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.SimulationInstanceRemovalListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WebotsSimulationInstanceService implements SimulationInstanceService {
    private static final Logger log = LogManager.getLogger();
    private static final Set<SimulationInstanceRemovalListener> SIMULATION_INSTANCE_REMOVAL_LISTENERS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<SimulationInstanceConfig> instances = Collections.synchronizedSet(new HashSet<>());
    private final String adaptorName;
    public WebotsSimulationInstanceService(@Value("${adaptor.endpoint.name}") String adaptorName) {
       this.adaptorName = adaptorName;
    }

    @Override
    public void addSimulationInstance(SimulationInstanceConfig config) throws BadRequestException {
        if(!config.getSimulationType().equals(this.adaptorName)){
            throw new BadRequestException("Simulation name does not match the name of this adaptor");
        }
        if(instances.stream().anyMatch(instance -> instance.getInstanceHost().equals(config.getInstanceHost()) &&
                instance.getInstancePort().equals(config.getInstancePort()))){
            throw new BadRequestException("Simulation instance with same host and port already exists");
        }
        if(instances.stream().anyMatch(instance -> instance.getInstanceId().equals(config.getInstanceId()))){
            throw new BadRequestException("Simulation instance with same id already exists");
        }
        instances.add(config);
        log.info("Added simulation instance: {}", config);
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

    public void addSimulationRemovalListener(SimulationInstanceRemovalListener listener) {
        SIMULATION_INSTANCE_REMOVAL_LISTENERS.add(listener);
        log.info("Added simulation instance removal listener: {}", listener);
    }

    public void notifySimulationRemovalListeners(SimulationInstanceConfig config) {
        SIMULATION_INSTANCE_REMOVAL_LISTENERS.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation instance removal listeners about removal of: {}", config);
    }

    public static Set<SimulationInstanceConfig> getInstances() {
        return instances;
    }
}
