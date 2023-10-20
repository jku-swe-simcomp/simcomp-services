package at.jku.swe.simcomp.serviceregistry.domain.mapper;

import at.jku.swe.simcomp.commons.adaptor.endpoint.dto.AdaptorEndpointDeclarationDTO;
import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import at.jku.swe.simcomp.serviceregistry.domain.model.AdaptorEndpoint;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface AdaptorMapper {
    AdaptorMapper INSTANCE = Mappers.getMapper(AdaptorMapper.class);

//    @Mapping(target = "adaptorEndpoints", qualifiedByName = "mapEndpointsWithAdaptor")
    Adaptor dtoToEntity(ServiceRegistrationConfigDTO serviceRegistrationConfigDTO);
    ServiceRegistrationConfigDTO entityToDto(Adaptor adaptor);

    AdaptorEndpointDeclarationDTO entityToDto(AdaptorEndpoint adaptorEndpoint);
    AdaptorEndpoint dtoToEntity(AdaptorEndpointDeclarationDTO adaptorEndpointDeclarationDTO);

    // TODO: not working currently, am working around it, try to make it working
//    @Named("mapEndpointsWithAdaptor")
//    default List<AdaptorEndpoint> mapEndpointsWithAdaptor(List<AdaptorEndpointDeclarationDTO> dtos, @Context Adaptor adaptor){
//        return dtos.stream()
//                .map(dto -> {
//                    AdaptorEndpoint entity = dtoToEntity(dto);
//                    entity.setAdaptor(adaptor);
//                    return entity;
//                })
//                .toList();
//    }

}
