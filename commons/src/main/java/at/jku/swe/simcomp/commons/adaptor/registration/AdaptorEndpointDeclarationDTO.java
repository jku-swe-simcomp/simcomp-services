package at.jku.swe.simcomp.commons.adaptor.registration;

import lombok.*;

/**
 * Declaration of an endpoint of an adaptor.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdaptorEndpointDeclarationDTO {
    /**
     * The type of the endpoint.
     */
    @NonNull
    private AdaptorEndpointType endpointType;
    /**
     * The path under which the endpoint is exposed.
     */
    @NonNull
    private String path;
}
