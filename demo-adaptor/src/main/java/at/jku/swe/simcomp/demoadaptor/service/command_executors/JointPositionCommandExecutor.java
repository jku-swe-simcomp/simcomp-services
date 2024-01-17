package at.jku.swe.simcomp.demoadaptor.service.command_executors;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.springframework.stereotype.Service;

import static at.jku.swe.simcomp.demoadaptor.service.DemoSimulationInstanceService.currentJointPositions;

@Service
public class JointPositionCommandExecutor implements CommandExecutor<ExecutionCommand.SetJointPositionCommand, ExecutionResultDTO> {

    @Override
    public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionCommand command, SimulationInstanceConfig config) {
        var jointPositions = currentJointPositions.get(config);
        if(jointPositions != null){
            int jointIndex = command.jointPosition().getJoint().getIndex() - 1;
            jointPositions.set(jointIndex, command.jointPosition().getRadians());
        }
        return ExecutionResultDTO.builder()
                .report("Did set joint position of axis %s to %s.".formatted(command.jointPosition().getJoint(), command.jointPosition().getRadians()))
                .build();
    }
}
