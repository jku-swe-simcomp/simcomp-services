package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationDisplayDTO;
import at.jku.swe.simcomp.manager.rest.exception.BadRequestException;
import at.jku.swe.simcomp.manager.service.client.AdaptorClient;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimulationService {
    private final ServiceRegistryClient serviceRegistryClient;
    private final AdaptorClient adaptorClient;

    public SimulationService(ServiceRegistryClient serviceRegistryClient, AdaptorClient adaptorClient){
       this.serviceRegistryClient = serviceRegistryClient;
        this.adaptorClient = adaptorClient;
    }

    public List<ServiceRegistrationDisplayDTO> getAvailableSimulations(){
        return serviceRegistryClient.getRegisteredAdaptors()
                .stream()
                .map(ServiceRegistrationConfigDTO::viewForDisplay)
                .toList();
    }

    public void registerSimulationInstanceForAdaptor(String name, SimulationInstanceConfig config) throws NotFoundException {
        Optional<ServiceRegistrationConfigDTO> adaptorConfig = serviceRegistryClient.getRegisteredAdaptors().stream()
                .filter(ac -> ac.getName().equals(name))
                        .findFirst();

        if(adaptorConfig.isPresent()){
            adaptorClient.registerSimulationInstanceForAdaptor(adaptorConfig.get(), config);
        }else{
            throw new NotFoundException("Simulation with name " + name + " not found.");
        }

    }

    public Map<String, List<SimulationInstanceConfig>> getSimulationInstances() {
        List<SimulationInstanceConfig> instances = new ArrayList<>();
        return serviceRegistryClient.getRegisteredAdaptors().stream()
                .collect(Collectors.toMap(
                        ServiceRegistrationConfigDTO::getName,
                        this::getSimulationInstances,
                        (list1, list2) -> { // will not happen as no duplicate keys possible in service registry
                            list1.addAll(list2);
                            return list1;
                        }
                ));
    }
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

    public void deleteSimulationInstance(String simulationName, SimulationInstanceConfig config){
        Optional<ServiceRegistrationConfigDTO> adaptorConfig = serviceRegistryClient.getRegisteredAdaptors().stream()
                .filter(ac -> ac.getName().equals(simulationName))
                .findFirst();

        if(adaptorConfig.isPresent()){
            adaptorClient.deleteSimulationInstance(adaptorConfig.get(), config);
        }else{
            throw new NotFoundException("Simulation with name " + simulationName + " not found.");
        }
    }

    private List<SimulationInstanceConfig> getSimulationInstances(ServiceRegistrationConfigDTO config){
        return adaptorClient.getSimulationInstances(config);
    }
}
