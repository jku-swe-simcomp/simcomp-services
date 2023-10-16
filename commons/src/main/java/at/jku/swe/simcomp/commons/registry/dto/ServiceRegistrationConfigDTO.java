package at.jku.swe.simcomp.commons.registry.dto;

import at.jku.swe.simcomp.commons.adaptor.endpoint.dto.AdaptorEndpointDeclarationDTO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRegistrationConfigDTO {
    @NonNull
    private String name;
    @NonNull
    private String host;
    @NonNull
    private String port;
    @NonNull
    private List<AdaptorEndpointDeclarationDTO> adaptorEndpoints;
}
