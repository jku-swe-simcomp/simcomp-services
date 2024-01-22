package at.jku.swe.simcomp.webotsdroneadaptor.domain.simulation;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

/**
 * Interface to listen to the closing of session and closes the session
 * if it is managed by the instance of this interface.
 */
public interface DroneInstanceRemovalListener {
    void onSimulationRemoved(SimulationInstanceConfig config);
}
