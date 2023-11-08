package at.jku.swe.simcomp.webotsadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboStateDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.webotsadaptor.domain.simulation.WebotsSimulationConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PoseCommandExecutor implements CommandExecutor<ExecutionCommand.PoseCommand, WebotsSimulationConfig, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.PoseCommand command, WebotsSimulationConfig config) {
        return ExecutionResultDTO.builder()
                .report("Pose command executed")
                .currentState(RoboStateDTO.builder()
                        .pose(PoseDTO.builder()
                                .position(command.position())
                                .orientation(command.orientation())
                                .build())
                        .jointStates(new ArrayList<>())
                        .build())
                .build();
    }
}