package at.jku.swe.simcomp.commons.adaptor.endpoint.simulation;

import java.util.Set;

/**
 * This interface defines the methods for the simulation instance management.
 * Has to be implemented by the simulation adaptor.
 */
public interface SimulationInstanceService {
    void addSimulationInstance(SimulationInstanceConfig config) throws Exception;

    void removeSimulationInstance(String instanceId) throws Exception;

    Set<SimulationInstanceConfig> getSimulationInstances() throws Exception;
}
