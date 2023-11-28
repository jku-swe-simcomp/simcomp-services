package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationInstanceConfig {
    @NonNull
    private String simulationType;
    @NonNull
    private String instanceId;
    @NonNull
    private String instanceHost;
    @NonNull
    private Integer instancePort;
}
