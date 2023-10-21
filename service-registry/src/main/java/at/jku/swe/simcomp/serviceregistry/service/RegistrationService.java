package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.domain.model.AdaptorStatus;
import at.jku.swe.simcomp.serviceregistry.domain.repository.AdaptorRepository;
import at.jku.swe.simcomp.serviceregistry.rest.exceptions.AdaptorAlreadyRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RegistrationService {
    private final AdaptorRepository adaptorRepository;
    private final AdaptorMapper adaptorMapper;
    public RegistrationService(AdaptorRepository adaptorRepository, AdaptorMapper adaptorMapper){
        this.adaptorRepository = adaptorRepository;
        this.adaptorMapper = adaptorMapper;
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
        Adaptor adaptor = adaptorMapper.dtoToEntity(config);
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
    public List<ServiceRegistrationConfigDTO> getAllHealthyRegisteredAdaptors(){
       return adaptorRepository.findAll()
               .stream()
               .filter(adaptor -> adaptor.getStatus().equals(AdaptorStatus.HEALTHY))
               .map(adaptorMapper::entityToDto).toList();
    }
}
