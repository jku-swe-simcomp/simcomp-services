package at.jku.swe.simcomp.azureadapter.config;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ActionType;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

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
                .supportedActions(Set.of(ActionType.SET_JOINT_POSITION,
                        ActionType.ADJUST_JOINT_ANGLE))
                .build();
    }
}
