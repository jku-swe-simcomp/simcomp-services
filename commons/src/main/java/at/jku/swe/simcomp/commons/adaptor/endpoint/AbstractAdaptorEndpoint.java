package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResponseDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistryClient;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import org.springframework.http.ResponseEntity;

import javax.annotation.PreDestroy;


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
        try {
            serviceRegistryClient.unregister(this.serviceRegistrationConfigDTO.getName());
        } catch (ServiceRegistrationFailedException e) {
            // Note: Logging currently not working in commons so that it shows up in projects that depend on it
            System.out.println("Could not unregister from service with message: %s".formatted(e.getMessage()));
        }
    }

    private void registerThisAdaptorEndpointAtServiceRegistry() {
        try{
            this.serviceRegistryClient.register(this.serviceRegistrationConfigDTO);
        }catch(Exception e){//we want to catch all possible exceptions as this method is called during object initialization
            System.out.println("Could not register adaptor endpoint at service registry.");
        }
    }
}
