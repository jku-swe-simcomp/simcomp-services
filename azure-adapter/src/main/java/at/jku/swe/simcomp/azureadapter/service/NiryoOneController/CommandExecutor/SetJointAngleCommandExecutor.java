package at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.CommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.springframework.stereotype.Service;

/**
 * The SetJointAngleCommandExecutor class implements the CommandExecutor interface
 * for executing SetJointPositionCommand and setting joint angles in the Azure environment.
 */
@Service
public class SetJointAngleCommandExecutor implements CommandExecutor<ExecutionCommand.SetJointPositionCommand, ExecutionResultDTO> {

    /**
     * Executes the SetJointPositionCommand and sets the joint angle in the Azure environment.
     *
     * @param command The SetJointPositionCommand to be executed.
     * @param config  The SimulationInstanceConfig providing configuration details for the simulation instance.
     * @return An ExecutionResultDTO indicating the success of the command execution.
     * @throws Exception If an error occurs during the execution of the command.
     */
    @Override
    public ExecutionResultDTO execute(ExecutionCommand.SetJointPositionCommand command, SimulationInstanceConfig config) throws Exception {
        switch (command.jointPosition().getJoint()) {
            case AXIS_1 ->
                    AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint1_angle", command.jointPosition().getRadians());
            case AXIS_2 ->
                    AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint2_angle", command.jointPosition().getRadians());
            case AXIS_3 ->
                    AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint3_angle", command.jointPosition().getRadians());
            case AXIS_4 ->
                    AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint4_angle", command.jointPosition().getRadians());
            case AXIS_5 ->
                    AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint5_angle", command.jointPosition().getRadians());
            case AXIS_6 ->
                    AzureExecutionService.setJointAngle(config.getInstanceId(), "/joint6_angle", command.jointPosition().getRadians());
            default -> throw new IllegalArgumentException();
        }
        return new ExecutionResultDTO("Success");
    }
}
