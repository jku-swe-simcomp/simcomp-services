package at.jku.swe.simcomp.webotsdroneadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceService;
import at.jku.swe.simcomp.webotsdroneadaptor.domain.simulation.DroneInstanceRemovalListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The class keeps track of the all simulation instances registered
 * for the Webots adapter. The class contains a set of all simulations
 * and allows to add and remove instances. The class contains a static
 * set of
 * @see SimulationInstanceConfig and
 * @see DroneInstanceRemovalListener
 */
@Service
public class WebotsDroneSimulationInstanceService implements SimulationInstanceService {
    private static final Logger log = LogManager.getLogger();
    private static final Set<DroneInstanceRemovalListener> SIMULATION_INSTANCE_REMOVAL_LISTENERS = Collections.synchronizedSet(new HashSet<>());
    private static final Set<SimulationInstanceConfig> INSTANCES = Collections.synchronizedSet(new HashSet<>());
    private final String adaptorName;

    /**
     * Creates a new instance, the value of the adaptor name must
     * be equal to WEBOTS
     * @param adaptorName the name of the adaptor
     */
    public WebotsDroneSimulationInstanceService(@Value("${adaptor.endpoint.name}") String adaptorName) {
       this.adaptorName = adaptorName;
    }

    /**
     * Method to add a new simulation instance based on a configuration.
     * The method checks, if the simulation is of type Webots, and if
     * instanceID, host and port a new.
     * @param config Configuration with the simulation name, simulation
     *               type, host and port
     * @throws BadRequestException gets thrown if the simulation instance
     * has the wrong type, the host and port a already used, or the instance
     * ID already exists.
     */
    @Override
    public void addSimulationInstance(SimulationInstanceConfig config) throws BadRequestException {
        if(!config.getSimulationType().equals(this.adaptorName)){
            throw new BadRequestException("Simulation name does not match the name of this adaptor");
        }
        if(INSTANCES.stream().anyMatch(instance -> instance.getInstanceHost().equals(config.getInstanceHost()) &&
                instance.getInstancePort().equals(config.getInstancePort()))){
            throw new BadRequestException("Simulation instance with same host and port already exists");
        }
        if(INSTANCES.stream().anyMatch(instance -> instance.getInstanceId().equals(config.getInstanceId()))){
            throw new BadRequestException("Simulation instance with same id already exists");
        }
        INSTANCES.add(config);
        log.info("Added simulation instance: {}", config);
    }

    /**
     * Removes a simulation instance from the set of instance, and
     * calls the simulation removal listener that the instance got removed.
     * @param instanceId the ID of the instance to remove.
     */
    @Override
    public void removeSimulationInstance(String instanceId) {
        Optional<SimulationInstanceConfig> config = INSTANCES.stream().filter(simulation -> simulation.getInstanceId().equals(instanceId)).findFirst();
        if(config.isPresent()){
            INSTANCES.remove(config.get());
            notifySimulationRemovalListeners(config.get());
            log.info("Removed simulation: {}", config.get());
        }else {
            log.info("Simulation instance {} not found", instanceId);
        }
    }

    /**
     * Nonstatic method to return the set of instances.
     * @return the set of instances.
     */
    @Override
    public Set<SimulationInstanceConfig> getSimulationInstances() {
        return INSTANCES;
    }

    /**
     * Adds a new removal listener to the static set of removal listeners.
     * @param listener the new removal listener to add
     */
    public void addSimulationRemovalListener(DroneInstanceRemovalListener listener) {
        SIMULATION_INSTANCE_REMOVAL_LISTENERS.add(listener);
        log.info("Added simulation instance removal listener: {}", listener);
    }

    /**
     * Notifies alls removal listener that a simulation instance was removed.
     * @param config the simulation instance that was removed.
     */
    public void notifySimulationRemovalListeners(SimulationInstanceConfig config) {
        SIMULATION_INSTANCE_REMOVAL_LISTENERS.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation instance removal listeners about removal of: {}", config);
    }

    /**
     * Static method to return the set of instances.
     * @return the set of instances.
     */
    public static Set<SimulationInstanceConfig> getInstances() {
        return INSTANCES;
    }
}
