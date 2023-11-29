package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationInstanceConfig {
    @NonNull
    @Schema(description = "The type of the simulation. e.g. WEBOTS", example = "WEBOTS")
    private String simulationType;
    @NonNull
    @Schema(description = "The id of the simulation instance.", example = "MY_PERSONAL_WEBOTS_INSTANCE")
    private String instanceId;
    @NonNull
    @Schema(description = "The host of the simulation instance.", example = "127.0.0.1")
    private String instanceHost;
    @NonNull
    @Schema(description = "The port of the simulation instance.", example = "10010")
    private Integer instancePort;
}
