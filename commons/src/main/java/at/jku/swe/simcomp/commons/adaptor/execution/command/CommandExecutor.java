package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

public interface CommandExecutor<T extends ExecutionCommand, S extends ExecutionResultDTO> {
   public S execute(T command, SimulationInstanceConfig config) throws Exception;
}
