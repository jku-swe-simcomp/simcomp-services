package at.jku.swe.simcomp.commons.registry;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ServiceRegistryClient {

    public void register(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO){
        // register automatically at client
        String serviceRegistryEndpointUrl = System.getenv("SERVICE_REGISTRY_ENDPOINT");
        // send request to register at registry
    }
    public void unregister(String serviceName){
        // register automatically at client
        String serviceRegistryEndpointUrl = System.getenv("SERVICE_REGISTRY_ENDPOINT");
        // send request to unregister from registry
    }
}
