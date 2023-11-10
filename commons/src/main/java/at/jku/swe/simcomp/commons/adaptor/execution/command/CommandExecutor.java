package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.executor.ExecutorDefinitionConfig;

public interface CommandExecutor<T extends ExecutionCommand, R extends ExecutorDefinitionConfig, S extends ExecutionResultDTO> {
   public S execute(T command, R config) throws Exception;
}
