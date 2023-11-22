package at.jku.swe.simcomp.demoadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.springframework.stereotype.Service;

@Service
public class AdjustJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.AdjustJointAngleCommand, ExecutionResultDTO> {
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, SimulationInstanceConfig config) {
        return ExecutionResultDTO.builder()
                .report("AdjustJointAngle command executed")
                .build();
    }
}
