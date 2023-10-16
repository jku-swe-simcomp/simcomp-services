package at.jku.swe.simcomp.commons.adaptor.endpoint.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdaptorEndpointDeclarationDTO {
    @NonNull
    private AdaptorEndpointType endpointType;
    @NonNull
    private String path;
}
