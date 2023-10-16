package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.registry.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import javax.annotation.PreDestroy;
import java.util.logging.Level;


@Log
public abstract class AbstractAdaptorEndpoint {
    private static final boolean IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT = true;
    protected final ServiceRegistryClient serviceRegistryClient = new ServiceRegistryClient();
    protected final ServiceRegistrationConfigDTO serviceRegistrationConfigDTO;

    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO){
        this(serviceRegistrationConfigDTO, IS_AUTO_REGISTRATION_ENABLED_BY_DEFAULT);
    }

    protected AbstractAdaptorEndpoint(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO, boolean isAutoRegistrationEnabled){
        this.serviceRegistrationConfigDTO=serviceRegistrationConfigDTO;
        if(isAutoRegistrationEnabled){
           registerThisAdaptorEndpointAtServiceRegistry();
        }
    }

    public abstract ResponseEntity<ExecutionResponseDTO> executeAction(ExecutionCommandDTO executionCommandDTO);
    public abstract ResponseEntity<String> getAttribute();
    public abstract ResponseEntity<Void> healthCheck();

    @PreDestroy
    public void onDestroy(){
        serviceRegistryClient.unregister(this.serviceRegistrationConfigDTO.getName());
    }

    private void registerThisAdaptorEndpointAtServiceRegistry() {
        try{
            this.serviceRegistryClient.register(this.serviceRegistrationConfigDTO);
        }catch(Exception e){
            log.log(Level.WARNING, "Could not register adaptor endpoint at service registry.");
        }
    }
}
