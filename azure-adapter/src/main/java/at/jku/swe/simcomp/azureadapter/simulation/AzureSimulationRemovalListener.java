package at.jku.swe.simcomp.azureadapter.simulation;

import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

/**
 * The {@code AzureSimulationRemovalListener} interface defines a contract for classes
 * that wish to be notified when a simulation instance is removed in an Azure environment.
 *
 * <p>Implementations of this interface must provide the {@link #onSimulationRemoved(SimulationInstanceConfig)}
 * method to handle the event of a simulation instance being removed.</p>
 *
 * <p>This interface is typically used in conjunction with the {@code AzureSimulationInstanceService}
 * to receive notifications about the removal of simulation instances.</p>
 *
 */
public interface AzureSimulationRemovalListener {

    /**
     * Called when a simulation instance is removed, providing the configuration of the removed instance.
     *
     * @param config The configuration of the simulation instance that has been removed.
     */
    void onSimulationRemoved(SimulationInstanceConfig config);
}
