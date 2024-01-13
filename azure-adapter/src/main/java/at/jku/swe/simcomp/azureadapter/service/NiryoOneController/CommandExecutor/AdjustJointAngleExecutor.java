package at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.springframework.stereotype.Service;

@Service
public class AdjustJointAngleExecutor implements CommandExecutor<ExecutionCommand.AdjustJointAngleCommand, ExecutionResultDTO> {
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.AdjustJointAngleCommand command, SimulationInstanceConfig config) {
        double currentvalue;
        switch (command.jointAngleAdjustment().getJoint()) {
            case AXIS_1 -> {
                currentvalue = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint1_angle");
                AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint1_angle", command.jointAngleAdjustment().getByRadians() + currentvalue);
            }
            case AXIS_2 -> {
                currentvalue = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint2_angle");
                AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint2_angle", command.jointAngleAdjustment().getByRadians() + currentvalue);
            }
            case AXIS_3 -> {
                currentvalue = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint3_angle");
                AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint3_angle", command.jointAngleAdjustment().getByRadians() + currentvalue);
            }
            case AXIS_4 -> {
                currentvalue = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint4_angle");
                AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint4_angle", command.jointAngleAdjustment().getByRadians() + currentvalue);
            }
            case AXIS_5 -> {
                currentvalue = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint5_angle");
                AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint5_angle", command.jointAngleAdjustment().getByRadians() + currentvalue);
            }
            case AXIS_6 -> {
                currentvalue = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint6_angle");
                AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint6_angle", command.jointAngleAdjustment().getByRadians() + currentvalue);
            }
            default -> throw new IllegalArgumentException();
        }
        return new ExecutionResultDTO("Success");
    }
}
