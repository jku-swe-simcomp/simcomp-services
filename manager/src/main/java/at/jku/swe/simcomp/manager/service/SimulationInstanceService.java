package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationDisplayDTO;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This service is responsible for managing simulation and simulation instances.
 */
@Service
public class SimulationInstanceService {
    private final ServiceRegistryClient serviceRegistryClient;
    private final AdaptorClient adaptorClient;

    /**
     * Constructor
     * @param serviceRegistryClient the service registry client
     * @param adaptorClient the adaptor client
     */
    public SimulationInstanceService(ServiceRegistryClient serviceRegistryClient, AdaptorClient adaptorClient){
       this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorClient = adaptorClient;
    }

    /**
     * Returns all available simulations (types).
     * @return the available simulations
     */
    public List<ServiceRegistrationDisplayDTO> getAvailableSimulations(){
        return serviceRegistryClient.getRegisteredAdaptors()
                .stream()
                .map(ServiceRegistrationConfigDTO::viewForDisplay)
                .toList();
    }

    /**
     * Registers a simulation instance for a given simulation type.
     * @param config the simulation instance config
     * @throws Exception if registration fails
     * @throws NotFoundException if the simulation type is not found
     */
    public void registerSimulationInstanceForAdaptor(SimulationInstanceConfig config) throws Exception {
        Optional<ServiceRegistrationConfigDTO> adaptorConfig = serviceRegistryClient.getRegisteredAdaptors().stream()
                .filter(ac -> ac.getName().equals(config.getSimulationType()))
                        .findFirst();

        if(adaptorConfig.isPresent()){
            adaptorClient.registerSimulationInstanceForAdaptor(adaptorConfig.get(), config);
        }else{
            throw new NotFoundException("Simulation with name " + config.getSimulationType() + " not found.");
        }
    }

    /**
     * Returns all simulation instances for all available simulation types.
     * @return the simulation instances
     */
    public List<SimulationInstanceConfig> getSimulationInstances() {
        List<SimulationInstanceConfig> instances = new ArrayList<>();
        serviceRegistryClient.getRegisteredAdaptors().forEach(ac -> instances.addAll(getSimulationInstances(ac)));
        return instances;
    }

    /**
     * Returns all simulation instances for a given simulation type.
     * @param name the simulation type
     * @return the simulation instances
     * @throws NotFoundException if the simulation type is not found
     */
    public List<SimulationInstanceConfig> getSimulationInstances(String name){
        Optional<ServiceRegistrationConfigDTO> adaptorConfig = serviceRegistryClient.getRegisteredAdaptors().stream()
                .filter(ac -> ac.getName().equals(name))
                .findFirst();

        if(adaptorConfig.isPresent()){
            return getSimulationInstances(adaptorConfig.get());
        }else{
            throw new NotFoundException("Simulation with name " + name + " not found.");
        }
    }

    /**
     * Deletes a simulation instance for a given simulation type.
     * @param simulationName the simulation type
     * @param instanceId the simulation instance id
     * @throws NotFoundException if the simulation type is not found
     */
    public void deleteSimulationInstance(String simulationName, String instanceId){
        Optional<ServiceRegistrationConfigDTO> adaptorConfig = serviceRegistryClient.getRegisteredAdaptors().stream()
                .filter(ac -> ac.getName().equals(simulationName))
                .findFirst();

        if(adaptorConfig.isPresent()){
            adaptorClient.deleteSimulationInstance(adaptorConfig.get(), instanceId);
        }else{
            throw new NotFoundException("Simulation with name " + simulationName + " not found.");
        }
    }

    private List<SimulationInstanceConfig> getSimulationInstances(ServiceRegistrationConfigDTO config){
        return adaptorClient.getSimulationInstances(config);
    }
}
