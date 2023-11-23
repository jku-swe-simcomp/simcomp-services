package at.jku.swe.simcomp.webotsadaptor.domain.simulation;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

public interface SimulationInstanceRemovalListener {
    void onSimulationRemoved(SimulationInstanceConfig config);
}
