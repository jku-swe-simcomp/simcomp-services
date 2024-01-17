package at.jku.swe.simcomp.demoadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.springframework.stereotype.Service;

import static at.jku.swe.simcomp.demoadaptor.service.DemoSimulationInstanceService.currentJointPositions;

@Service
public class AdjustJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.AdjustJointAngleCommand, ExecutionResultDTO> {
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, SimulationInstanceConfig config) {
        var jointPositions = currentJointPositions.get(config);
        if(jointPositions != null){
            int jointIndex = command.jointAngleAdjustment().getJoint().getIndex() - 1;
            jointPositions.set(jointIndex, jointPositions.get(jointIndex) + command.jointAngleAdjustment().getByRadians());
        }
        return ExecutionResultDTO.builder()
                .report("AdjustJointAngle command executed")
                .build();
    }
}
