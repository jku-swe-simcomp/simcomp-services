package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.commons.adaptor.registration.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.domain.model.SupportedActionType;
import at.jku.swe.simcomp.serviceregistry.domain.repository.AdaptorRepository;
import at.jku.swe.simcomp.serviceregistry.rest.exceptions.AdaptorAlreadyRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RegistrationService {
    private final AdaptorRepository adaptorRepository;
    public RegistrationService(AdaptorRepository adaptorRepository){
        this.adaptorRepository = adaptorRepository;
    }

    /**
     * Persists the registration in the database.
     * @param config the config
     * @throws AdaptorAlreadyRegisteredException if a registration with the same name is already in the database.
     */
    public void register(ServiceRegistrationConfigDTO config) throws AdaptorAlreadyRegisteredException {
        if(adaptorRepository.findById(config.getName()).isPresent()){
            throw new AdaptorAlreadyRegisteredException("The adaptor with the name %s is already registered".formatted(config.getName()));
        }
        Adaptor adaptor = dtoToEntity(config);
        adaptorRepository.save(adaptor);
        log.info("Registered service {}", adaptor);
    }

    /**
     * Deletes the registration for the adaptor with the passed name
     * @param adaptorId the name/id of the adaptor.
     */
    public void unregister(String adaptorId){
        adaptorRepository.deleteById(adaptorId);
        log.info("Unregistered service with name {}", adaptorId);
    }

    /**
     * Returns all registrations available.
     * @return the list with all registrations
     */
    public List<ServiceRegistrationConfigDTO> getAllRegisteredAdaptors(){
       return adaptorRepository.findAll().stream().map(this::entityToDto).toList();
    }

    private ServiceRegistrationConfigDTO entityToDto(Adaptor adaptor){
        ServiceRegistrationConfigDTO config = new ServiceRegistrationConfigDTO();
        config.setName(adaptor.getName());
        config.setHost(adaptor.getHost());
        config.setPort(adaptor.getPort());
        config.setBaseEndpoint(adaptor.getBaseEndpoint());
        config.setBaseEndpoint(adaptor.getBaseEndpoint());
        config.setSupportedActions(adaptor.getSupportedActions().stream()
                .map(SupportedActionType::getActionType).toList());
        return config;
    }
    private Adaptor dtoToEntity(ServiceRegistrationConfigDTO config){
        Adaptor adaptor = new Adaptor();
        adaptor.setName(config.getName());
        adaptor.setHost(config.getHost());
        adaptor.setPort(config.getPort());
        adaptor.setBaseEndpoint(config.getBaseEndpoint());
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
