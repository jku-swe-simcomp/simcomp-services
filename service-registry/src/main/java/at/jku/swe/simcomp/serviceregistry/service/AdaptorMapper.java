package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.domain.model.AdaptorStatus;
import at.jku.swe.simcomp.serviceregistry.domain.model.SupportedActionType;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Mapper for mapping between DTOs and entities, for {@link Adaptor} and {@link ServiceRegistrationConfigDTO}.
 */
@Service
public class AdaptorMapper {
    /**
     * Maps an {@link Adaptor} to a {@link ServiceRegistrationConfigDTO}.
     * @param adaptor the adaptor to map
     * @return the mapped dto
     */
    public ServiceRegistrationConfigDTO entityToDto(Adaptor adaptor){
        ServiceRegistrationConfigDTO config = new ServiceRegistrationConfigDTO();
        config.setName(adaptor.getName());
        config.setHost(adaptor.getHost());
        config.setPort(adaptor.getPort());
        config.setSupportedActions(adaptor.getSupportedActions().stream()
                .map(SupportedActionType::getActionType).collect(Collectors.toSet()));
        return config;
    }

    /**
     * Maps a {@link ServiceRegistrationConfigDTO} to an {@link Adaptor}.
     * @param config the dto to map
     * @return the mapped entity
     */
    public Adaptor dtoToEntity(ServiceRegistrationConfigDTO config){
        Adaptor adaptor = new Adaptor();
        adaptor.setName(config.getName());
        adaptor.setHost(config.getHost());
        adaptor.setPort(config.getPort());
        adaptor.setStatus(AdaptorStatus.HEALTHY);
        adaptor.setSupportedActions(config.getSupportedActions().stream()
                .map(type -> {
                    SupportedActionType supportedActionType = new SupportedActionType();
                    supportedActionType.setActionType(type);
                    supportedActionType.setAdaptor(adaptor);
                    return  supportedActionType;
                }).toList());
        adaptor.getSupportedActions()
                .forEach(supportedAction -> supportedAction.setAdaptor(adaptor));

        return adaptor;
    }
}
