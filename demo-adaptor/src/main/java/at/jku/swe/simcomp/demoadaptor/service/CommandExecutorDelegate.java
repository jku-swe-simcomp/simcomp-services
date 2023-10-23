package at.jku.swe.simcomp.demoadaptor.service;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionCommandDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.demoadaptor.domain.simulation.DemoSimulationConfig;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.AdjustJointAnglesCommandExecutor;
import at.jku.swe.simcomp.demoadaptor.service.command_executors.PoseCommandExecutor;
import org.springframework.stereotype.Service;

@Service
public class CommandExecutorDelegate implements CommandExecutor<ExecutionCommandDTO, DemoSimulationConfig, ExecutionResultDTO> {
    private final PoseCommandExecutor poseCommandExecutor;
    private final AdjustJointAnglesCommandExecutor adjustJointAnglesCommandExecutor;
    public CommandExecutorDelegate(PoseCommandExecutor poseCommandExecutor, AdjustJointAnglesCommandExecutor adjustJointAnglesCommandExecutor) {
        this.poseCommandExecutor = poseCommandExecutor;
        this.adjustJointAnglesCommandExecutor = adjustJointAnglesCommandExecutor;
    }

    @Override
    public ExecutionResultDTO execute(ExecutionCommandDTO command, DemoSimulationConfig config) {
        return switch(command.getActionType()) {
            case POSE -> poseCommandExecutor.execute(command.viewAsPoseCommand(), config);
            case ADJUST_JOINT_ANGLES -> adjustJointAnglesCommandExecutor.execute(command.viewAsAdjustJointAngleCommand(), config);
            default -> throw new UnsupportedOperationException("Unexpected value: " + command.getActionType());
        };
    }
}
