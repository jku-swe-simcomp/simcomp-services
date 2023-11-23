package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import java.util.Set;

public interface SimulationInstanceService {
    void addSimulationInstance(SimulationInstanceConfig config) throws Exception;

    void removeSimulationInstance(String instanceId) throws Exception;

    Set<SimulationInstanceConfig> getSimulationInstances() throws Exception;
}
