package at.jku.swe.simcomp.commons.registry.dto;

import at.jku.swe.simcomp.commons.adaptor.endpoint.dto.AdaptorEndpointDeclarationDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ServiceRegistrationConfigDTO {
    private String host;
    private String port;
    private List<AdaptorEndpointDeclarationDTO> adaptorEndpoints;
}
