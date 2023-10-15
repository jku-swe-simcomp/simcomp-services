package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.registry.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import org.springframework.http.ResponseEntity;

import javax.annotation.PreDestroy;

public abstract class AbstractAdaptorEndpoint {
    protected final ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient();
    protected final ServiceRegistrationConfigDTO serviceRegistrationConfigDTO;

    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO){
        serviceRegistryClient.register(serviceRegistrationConfigDTO);
        this.serviceRegistrationConfigDTO=serviceRegistrationConfigDTO;
    }

    protected abstract ResponseEntity<ExecutionResponseDTO> executeAction(ExecutionCommandDTO executionCommandDTO);
    protected abstract ResponseEntity<String> getAttribute();
    protected abstract ResponseEntity<Void> healthCheck();

    @PreDestroy
    public void onDestroy(){
        serviceRegistryClient.unregister(this.serviceRegistrationConfigDTO.getName());
    }
}
