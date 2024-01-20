package at.jku.swe.simcomp.manager;


import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.OrientationDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.manager.service.KinematicsService;
import at.jku.swe.simcomp.manager.service.client.AxisConverterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KinematicsServiceTest {
    @Mock
    private AxisConverterClient axisConverterClient;
    @InjectMocks
    private KinematicsService kinematicsService;

    @Test
    public void poseToComposite() {
        // Arrange
        ExecutionCommand.PoseCommand poseCommand = new ExecutionCommand.PoseCommand(PositionDTO.builder().x(0.0).y(0.0).z(0.0).build(),
                OrientationDTO.builder().x(0.0).y(0.0).z(0.0).build());
        List<JointPositionDTO> jointPositionDTOList = new ArrayList<>();
        jointPositionDTOList.add(new JointPositionDTO());
        jointPositionDTOList.add(new JointPositionDTO());
        jointPositionDTOList.add(new JointPositionDTO());
        jointPositionDTOList.add(new JointPositionDTO());
        jointPositionDTOList.add(new JointPositionDTO());
        jointPositionDTOList.add(new JointPositionDTO());

        List<ExecutionCommand.SetJointPositionCommand> setJointPositionCommandList = new ArrayList<>();
        setJointPositionCommandList.add(new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()));
        setJointPositionCommandList.add(new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()));
        setJointPositionCommandList.add(new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()));
        setJointPositionCommandList.add(new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()));
        setJointPositionCommandList.add(new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()));
        setJointPositionCommandList.add(new ExecutionCommand.SetJointPositionCommand(new JointPositionDTO()));

        ExecutionCommand.CompositeCommand expectedCompositeCommand = new ExecutionCommand.CompositeCommand(setJointPositionCommandList);

        when(axisConverterClient.inverseKinematics(any())).thenReturn(jointPositionDTOList);
        // Assert
        assertEquals(expectedCompositeCommand, kinematicsService.poseToComposite(poseCommand));
    }


}
