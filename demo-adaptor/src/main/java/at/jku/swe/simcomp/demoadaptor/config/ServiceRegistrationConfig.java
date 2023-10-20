package at.jku.swe.simcomp.demoadaptor.config;

import at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointDeclarationDTO;
import at.jku.swe.simcomp.commons.adaptor.registration.AdaptorEndpointType;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Config class that produces a configuration bean of type
 * {@link ServiceRegistrationConfigDTO}
 */
@Configuration
public class ServiceRegistrationConfig {

    /**
     * The method producing the config bean.
     * The configured endpoints have to match the implemented {@link at.jku.swe.simcomp.commons.adaptor.endpoint.AbstractAdaptorEndpoint},
     * see {@link at.jku.swe.simcomp.demoadaptor.rest.AdaptorEndpointController}
     * @param name the name of the adaptor (has to be unique)
     * @param host the host of the adaptor
     * @param port the port of the adaptor
     * @return the config bean
     */
    @Bean
    public ServiceRegistrationConfigDTO getServiceRegistrationConfig(@Value("${adaptor.endpoint.name}") String name,
                                                                     @Value("${adaptor.endpoint.host}") String host,
                                                                     @Value("${server.port}") Integer port){
        return ServiceRegistrationConfigDTO.builder()
                .name(name)
                .host(host)
                .port(port)
                .adaptorEndpoints(new ArrayList<>(List.of(
                        AdaptorEndpointDeclarationDTO.builder()
                                .endpointType(AdaptorEndpointType.EXECUTE_ACTION)
                                .path("/api/execute")
                                .build(),
                        AdaptorEndpointDeclarationDTO.builder()
                                .endpointType(AdaptorEndpointType.GET_ATTRIBUTE)
                                .path("/api/attribute")
                                .build(),
                        AdaptorEndpointDeclarationDTO.builder()
                                .endpointType(AdaptorEndpointType.HEALTH_CHECK)
                                .path("/api/health")
                                .build()
                )))
                .build();
    }
}
