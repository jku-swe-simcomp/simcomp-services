package at.jku.swe.simcomp.azureadapter.simulation;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

public interface AzureSimulationRemovalListener {
    void onSimulationRemoved(SimulationInstanceConfig config);
}
