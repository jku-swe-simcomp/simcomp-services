package at.jku.swe.simcomp.serviceregistry.service;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.mapper.AdaptorMapper;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
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


    public void register(ServiceRegistrationConfigDTO config) throws AdaptorAlreadyRegisteredException {
        if(adaptorRepository.findById(config.getName()).isPresent()){
            throw new AdaptorAlreadyRegisteredException("The adaptor with the name %s is already registered".formatted(config.getName()));
        }

        Adaptor adaptor = AdaptorMapper.INSTANCE.dtoToEntity(config);
        log.info(adaptor.toString());

        // currently setting the reference to the parent in the endpoints manually as mapstruct approach not working
        adaptor.getAdaptorEndpoints()
                        .forEach(endpoint -> endpoint.setAdaptor(adaptor));

        adaptorRepository.save(adaptor);
    }

    public void unregister(String adaptorId){
        adaptorRepository.deleteById(adaptorId);
    }

    public List<ServiceRegistrationConfigDTO> getAllRegisteredAdaptors(){
       return adaptorRepository.findAll().stream().map(AdaptorMapper.INSTANCE::entityToDto).toList();
    }
}
