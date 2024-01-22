package at.jku.swe.simcomp.azureadapter.service.Services;

import at.jku.swe.simcomp.azureadapter.simulation.AzureSimulationRemovalListener;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The {@code AzureSimulationInstanceService} class is an implementation of the
 * {@link SimulationInstanceService} interface, providing functionalities to manage
 * simulation instances in an Azure environment.
 *
 * <p>This service is responsible for adding, removing, and retrieving simulation instances,
 * as well as handling cleanup operations during the destruction of the service bean.</p>
 *
 * <p>The service uses Azure-specific functionality provided by the {@link AzureService}
 * class for creating and deleting digital twins associated with simulation instances.</p>
 *
 * <p>Instances of this service can be configured with a specific adaptor name, and it
 * ensures that simulation instances added to the service match the configured adaptor name.</p>
 *
 * <p>This service also supports the registration of removal listeners to receive notifications
 * when simulation instances are removed.</p>
 *
 * <p>Instances and removal listeners are maintained in thread-safe data structures to handle
 * concurrent access.</p>
 *
 * <p>The service utilizes the SLF4J logging framework for logging information and errors.</p>
 *
 * <p><b>Note:</b> This class uses Lombok annotations, such as {@code @Slf4j}, for logging.</p>
 *
 */
@Service
@Slf4j
public class AzureSimulationInstanceService implements SimulationInstanceService {

    /**
     * A set of removal listeners to be notified when simulation instances are removed.
     */
    private static final Set<AzureSimulationRemovalListener> simulationInstanceRemovalListeners =
            Collections.synchronizedSet(new HashSet<>());

    /**
     * A set of simulation instances currently managed by this service.
     */
    public static final Set<SimulationInstanceConfig> instances = Collections.synchronizedSet(new HashSet<>());

    /**
     * The name of the adaptor associated with this service.
     */
    private final String adaptorName;

    /**
     * Constructs a new {@code AzureSimulationInstanceService} with the specified adaptor name.
     *
     * @param adaptorName The name of the adaptor associated with this service.
     */
    public AzureSimulationInstanceService(@Value("${adaptor.endpoint.name}") String adaptorName) {
        this.adaptorName = adaptorName;
    }

    /**
     * Adds a new simulation instance to the service.
     *
     * @param config The configuration of the simulation instance to be added.
     * @throws BadRequestException If the simulation name does not match the name of this adaptor,
     *                             or if a simulation instance with the same ID already exists.
     */
    @Override
    public void addSimulationInstance(SimulationInstanceConfig config) throws BadRequestException {
        if(!config.getSimulationType().equals(this.adaptorName)){
            throw new BadRequestException("Simulation name does not match the name of this adaptor");
        }
        if(instances.stream().anyMatch(instance -> instance.getInstanceId().equals(config.getInstanceId()))){
            throw new BadRequestException("Simulation instance with same id already exists");
        }
        AzureService.createDigitalTwin(config.getInstanceId());
        instances.add(config);
        log.info("Added simulation: {}", config);
    }

    /**
     * Removes a simulation instance from the service.
     *
     * @param instanceId The ID of the simulation instance to be removed.
     */
    @Override
    public void removeSimulationInstance(String instanceId) {
        Optional<SimulationInstanceConfig> config = instances.stream().filter(simulation ->
                simulation.getInstanceId().equals(instanceId)).findFirst();
        if(config.isPresent()){
            AzureService.deleteDigitalTwin(config.get().getInstanceId());
            instances.remove(config.get());
            notifySimulationRemovalListeners(config.get());
            log.info("Removed simulation: {}", config.get());
        }else {
            log.info("Simulation instance {} not found", instanceId);
        }
    }

    /**
     * Retrieves the set of simulation instances managed by this service.
     *
     * @return A set of simulation instance configurations.
     * @throws Exception If an error occurs while retrieving the simulation instances.
     */
    @Override
    public Set<SimulationInstanceConfig> getSimulationInstances() throws Exception {
        return instances;
    }

    /**
     * Adds a removal listener to receive notifications when simulation instances are removed.
     *
     * @param listener The removal listener to be added.
     */
    public void addSimulationRemovalListener(AzureSimulationRemovalListener listener) {
        simulationInstanceRemovalListeners.add(listener);
        log.info("Added simulation removal listener: {}", listener);
    }

    /**
     * Notifies all registered removal listeners about the removal of a simulation instance.
     *
     * @param config The configuration of the removed simulation instance.
     */
    public void notifySimulationRemovalListeners(SimulationInstanceConfig config) {
        simulationInstanceRemovalListeners.forEach(listener -> listener.onSimulationRemoved(config));
        log.info("Notified simulation instance removal listeners about removal of: {}", config);
    }

    /**
     * Retrieves the set of simulation instances currently managed by this service.
     *
     * @return A set of simulation instance configurations.
     */
    public static Set<SimulationInstanceConfig> getInstances() {
        return instances;
    }

    /**
     * Cleans up all simulation instances during bean destruction.
     */
    @PreDestroy
    public void cleanUpInstances() {
        for (var instance : instances) {
            AzureService.deleteDigitalTwin(instance.getInstanceId());
        }
    }
}