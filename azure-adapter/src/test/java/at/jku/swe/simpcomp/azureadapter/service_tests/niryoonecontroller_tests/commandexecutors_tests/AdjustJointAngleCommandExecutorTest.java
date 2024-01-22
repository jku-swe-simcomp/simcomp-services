package at.jku.swe.simpcomp.azureadapter.service_tests.niryoonecontroller_tests.commandexecutors_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.AdjustJointAngleCommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJoint;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdjustJointAngleCommandExecutorTest {

    @Test
    void execute_WithValidCommandAndConfig_ShouldReturnSuccessResult() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        JointAngleAdjustmentDTO jointAngleAdjustment = JointAngleAdjustmentDTO.builder().build();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(jointAngleAdjustment);
        SimulationInstanceConfig config = new SimulationInstanceConfig();

        ExecutionResultDTO result = executor.execute(command, config);

        assertNotNull(result);
        assertNotEquals("Success", result.getReport());
    }

    @Test
    void execute_WithInvalidCommand_ShouldThrowIllegalArgumentException() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        JointAngleAdjustmentDTO jointAngleAdjustment = JointAngleAdjustmentDTO.builder().build();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(jointAngleAdjustment);
        command.jointAngleAdjustment().setJoint(null);

        assertThrows(IllegalArgumentException.class, () -> executor.execute(command, new SimulationInstanceConfig()));
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldAdjustAxis1Angle() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        JointAngleAdjustmentDTO jointAngleAdjustment = JointAngleAdjustmentDTO.builder().build();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(jointAngleAdjustment);
        command.jointAngleAdjustment().setJoint(RoboJoint.AXIS_1);
        command.jointAngleAdjustment().setByRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        // Assert
        // Add assertions to check if the angle for AXIS_1 was adjusted as expected
        double adjustedAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint1_angle");
        assertEquals(1.0, adjustedAngle, 0.001); // Adjust the delta value based on your precision requirements
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldAdjustAxis2Angle() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        JointAngleAdjustmentDTO jointAngleAdjustment = JointAngleAdjustmentDTO.builder().build();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(jointAngleAdjustment);
        command.jointAngleAdjustment().setJoint(RoboJoint.AXIS_2);
        command.jointAngleAdjustment().setByRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        // Assert
        // Add assertions to check if the angle for AXIS_2 was adjusted as expected
        double adjustedAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint2_angle");
        assertEquals(1.0, adjustedAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldAdjustAxis3Angle() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(JointAngleAdjustmentDTO.builder().build());
        command.jointAngleAdjustment().setJoint(RoboJoint.AXIS_3);
        command.jointAngleAdjustment().setByRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        // Assert
        // Add assertions to check if the angle for AXIS_3 was adjusted as expected
        double adjustedAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint3_angle");
        assertEquals(1.0, adjustedAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldAdjustAxis4Angle() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(JointAngleAdjustmentDTO.builder().build());
        command.jointAngleAdjustment().setJoint(RoboJoint.AXIS_4);
        command.jointAngleAdjustment().setByRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        // Assert
        // Add assertions to check if the angle for AXIS_4 was adjusted as expected
        double adjustedAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint4_angle");
        assertEquals(1.0, adjustedAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldAdjustAxis5Angle() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(JointAngleAdjustmentDTO.builder().build());
        command.jointAngleAdjustment().setJoint(RoboJoint.AXIS_5);
        command.jointAngleAdjustment().setByRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        // Assert
        // Add assertions to check if the angle for AXIS_5 was adjusted as expected
        double adjustedAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint5_angle");
        assertEquals(1.0, adjustedAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldAdjustAxis6Angle() {
        AdjustJointAngleCommandExecutor executor = new AdjustJointAngleCommandExecutor();
        ExecutionCommand.AdjustJointAngleCommand command = new ExecutionCommand.AdjustJointAngleCommand(JointAngleAdjustmentDTO.builder().build());
        command.jointAngleAdjustment().setJoint(RoboJoint.AXIS_6);
        command.jointAngleAdjustment().setByRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        // Assert
        // Add assertions to check if the angle for AXIS_6 was adjusted as expected
        double adjustedAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint6_angle");
        assertEquals(1.0, adjustedAngle, 0.001);
    }
}
