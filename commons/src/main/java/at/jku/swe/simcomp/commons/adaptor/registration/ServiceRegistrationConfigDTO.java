package at.jku.swe.simcomp.commons.adaptor.registration;

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
    private Integer port;
    @NonNull
    private List<AdaptorEndpointDeclarationDTO> adaptorEndpoints;
}
