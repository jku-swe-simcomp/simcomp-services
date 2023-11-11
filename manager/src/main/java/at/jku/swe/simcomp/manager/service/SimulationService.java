package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationDisplayDTO;
import at.jku.swe.simcomp.manager.domain.repository.SessionRepository;
import at.jku.swe.simcomp.manager.service.client.ServiceRegistryClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SimulationService {
    private final ServiceRegistryClient serviceRegistryClient;

    public SimulationService(ServiceRegistryClient serviceRegistryClient){
       this.serviceRegistryClient = serviceRegistryClient;
    }

    public List<ServiceRegistrationDisplayDTO> getAvailableSimulations(){
        return serviceRegistryClient.getRegisteredAdaptors()
                .stream()
                .map(ServiceRegistrationConfigDTO::viewForDisplay)
                .toList();
    }
}
