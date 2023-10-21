package at.jku.swe.simcomp.demoadaptor.config;

import at.jku.swe.simcomp.commons.adaptor.dto.ActionType;
import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.demoadaptor.rest.DemoAdaptorEndpointController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Config class that produces a configuration bean of type
 * {@link ServiceRegistrationConfigDTO}
 */
@Configuration
public class ServiceRegistrationConfig {

    /**
     * The method producing the config bean.
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
                .baseEndpoint("/api")
                .supportedActions(Stream.of(ActionType.values())
                        .filter(type -> !type.equals(ActionType.CALIBRATE)) // all methods but calibrate supported by demo-adaptor
                        .toList())
                .build();
    }
}