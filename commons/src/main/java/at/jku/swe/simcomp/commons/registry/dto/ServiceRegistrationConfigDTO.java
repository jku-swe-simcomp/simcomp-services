package at.jku.swe.simcomp.commons.registry.dto;

import at.jku.swe.simcomp.commons.adaptor.dto.ActionType;
import lombok.*;

import java.util.Set;

/**
 * Configuration class to register an adaptor at the service registry.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRegistrationConfigDTO {
    /**
     * The name of the adaptor, has to be unique (is ID for registry).
     */
    @NonNull
    private String name;
    /**
     * The host of the adaptor.
     */
    @NonNull
    private String host;
    /**
     * The port of the adaptor.
     */
    @NonNull
    private Integer port;
    /**
     * List with details about the available endpoints.
     */
    @NonNull
    private Set<ActionType> supportedActions;
}
