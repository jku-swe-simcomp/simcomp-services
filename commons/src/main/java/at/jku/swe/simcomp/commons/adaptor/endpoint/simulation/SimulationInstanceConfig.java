package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationInstanceConfig {
    @NonNull
    private String simulationName;
    @NonNull
    private String instanceId;
    @NonNull
    private String instanceHost;
    @NonNull
    private Integer instancePort;
}
