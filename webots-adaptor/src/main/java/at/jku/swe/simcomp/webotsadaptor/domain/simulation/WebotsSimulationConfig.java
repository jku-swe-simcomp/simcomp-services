package at.jku.swe.simcomp.webotsadaptor.domain.simulation;

import at.jku.swe.simcomp.commons.adaptor.executor.ExecutorDefinitionConfig;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebotsSimulationConfig implements ExecutorDefinitionConfig {
    private String simulationEndpointUrl;
    private int simulationPort;
}
