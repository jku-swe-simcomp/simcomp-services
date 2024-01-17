package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;

/**
 * Utility interface for execution of a given command.
 * Can be implemented by the simulation adaptor to execute commands for better separation.
 * @param <T> Command to execute
 * @param <S> Result of the execution
 */
public interface CommandExecutor<T extends ExecutionCommand, S extends ExecutionResultDTO> {
   public S execute(T command, SimulationInstanceConfig config) throws Exception;
}
