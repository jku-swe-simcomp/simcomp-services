package at.jku.swe.simcomp.demoadaptor.domain.simulation;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionNotValidException;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationConfig;

public interface DemoSimulationRemovalListener {
    void onSimulationRemoved(DemoSimulationConfig config);
}
