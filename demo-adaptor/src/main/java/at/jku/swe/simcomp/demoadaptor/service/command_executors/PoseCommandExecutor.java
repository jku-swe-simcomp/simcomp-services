package at.jku.swe.simcomp.demoadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PoseCommandExecutor implements CommandExecutor<ExecutionCommand.PoseCommand, DemoSimulationConfig, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.PoseCommand command, DemoSimulationConfig config) {
        return ExecutionResultDTO.builder()
                .report("Pose command executed")
                .build();
    }
}
