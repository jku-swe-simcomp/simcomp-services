package at.jku.swe.simpcomp.azureadapter.service_tests.niryoonecontroller_tests.commandexecutors_tests;

import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.AzureExecutionService;
import at.jku.swe.simcomp.azureadapter.service.NiryoOneController.CommandExecutor.SetJointAngleCommandExecutor;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJoint;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SetJointAngleCommandExecutorTest {

    @Test
    void execute_WithValidCommandAndConfig_ShouldReturnSuccessResult() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        SimulationInstanceConfig config = new SimulationInstanceConfig();

        ExecutionResultDTO result = executor.execute(command, config);

        assertNotNull(result);
        assertNotEquals("Success", result.getReport());
    }

    @Test
    void execute_WithInvalidCommand_ShouldThrowIllegalArgumentException() {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(null);

        assertThrows(IllegalArgumentException.class, () -> executor.execute(command, new SimulationInstanceConfig()));
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldSetAxis1Angle() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(RoboJoint.AXIS_1);
        command.jointPosition().setRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        double setAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint1_angle");
        assertEquals(1.0, setAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldSetAxis2Angle() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(RoboJoint.AXIS_2);
        command.jointPosition().setRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        double setAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint2_angle");
        assertEquals(1.0, setAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldSetAxis3Angle() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(RoboJoint.AXIS_3);
        command.jointPosition().setRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        double setAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint3_angle");
        assertEquals(1.0, setAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldSetAxis4Angle() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(RoboJoint.AXIS_4);
        command.jointPosition().setRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        double setAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint4_angle");
        assertEquals(1.0, setAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldSetAxis5Angle() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(RoboJoint.AXIS_5);
        command.jointPosition().setRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        double setAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint5_angle");
        assertEquals(1.0, setAngle, 0.001);
    }

    @Test
    void execute_WithValidCommandAndConfig_ShouldSetAxis6Angle() throws Exception {
        SetJointAngleCommandExecutor executor = new SetJointAngleCommandExecutor();
        ExecutionCommand.SetJointPositionCommand command = new ExecutionCommand.SetJointPositionCommand(JointPositionDTO.builder().build());
        command.jointPosition().setJoint(RoboJoint.AXIS_6);
        command.jointPosition().setRadians(1.0);
        SimulationInstanceConfig config = new SimulationInstanceConfig();
        executor.execute(command, config);

        double setAngle = AzureExecutionService.getJointAngle(config.getInstanceId(), "joint6_angle");
        assertEquals(1.0, setAngle, 0.001);
    }
}
