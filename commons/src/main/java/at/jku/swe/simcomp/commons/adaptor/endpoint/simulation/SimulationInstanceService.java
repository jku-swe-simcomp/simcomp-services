package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import java.util.Set;

public interface SimulationInstanceService {
    void addSimulation(SimulationInstanceConfig config) throws Exception;

    void removeSimulation(SimulationInstanceConfig config) throws Exception;

    Set<SimulationInstanceConfig> getSimulationInstance() throws Exception;
}
