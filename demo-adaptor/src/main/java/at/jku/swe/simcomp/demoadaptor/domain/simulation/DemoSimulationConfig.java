package at.jku.swe.simcomp.demoadaptor.domain.simulation;

import at.jku.swe.simcomp.commons.adaptor.executor.ExecutorDefinitionConfig;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemoSimulationConfig implements ExecutorDefinitionConfig {
    private String simulationEndpointUrl;
}
