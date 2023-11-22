package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import at.jku.swe.simcomp.commons.adaptor.executor.ExecutorDefinitionConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulationInstanceConfig implements ExecutorDefinitionConfig {
    private String simulationEndpointUrl;
    private int simulationPort;
}
