package at.jku.swe.simcomp.demoadaptor.domain.simulation;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

public interface DemoSimulationRemovalListener {
    void onSimulationRemoved(SimulationInstanceConfig config);
}
