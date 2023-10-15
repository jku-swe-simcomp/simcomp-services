package at.jku.swe.simcomp.commons.adaptor.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdaptorEndpointDeclarationDTO {
    private AdaptorEndpointType endpointType;
    private String path;
}
