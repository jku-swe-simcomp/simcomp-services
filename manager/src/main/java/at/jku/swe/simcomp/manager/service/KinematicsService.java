package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.RoboJoint;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.manager.service.client.AxisConverterClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KinematicsService {

    private final AxisConverterClient axisConverterClient;

    public KinematicsService(AxisConverterClient axisConverterClient) {
        this.axisConverterClient = axisConverterClient;
    }

    public ExecutionCommand.CompositeCommand poseToComposite(ExecutionCommand.PoseCommand poseCommand){
        List<JointPositionDTO> jointPositions = axisConverterClient.inverseKinematics(new PoseDTO(poseCommand.position(), poseCommand.orientation()));
        if(jointPositions.size() != 6){
            throw new RuntimeException("Wrong number of joint positions for inverse kinematics returned by axis-converter.");
        }
        List<ExecutionCommand.SetJointPositionCommand> jointPositionCommands = jointPositions.stream()
                .map(ExecutionCommand.SetJointPositionCommand::new).toList();
        return new ExecutionCommand.CompositeCommand(jointPositionCommands);
    }

    public AttributeValue.Pose jointPositionsToPose(AttributeValue.JointPositions jointPositions){
        List<JointPositionDTO> jointPositionDTOs = toJointPositionDTOs(jointPositions);
        PoseDTO poseDTO = axisConverterClient.directKinematics(jointPositionDTOs);
        return new AttributeValue.Pose(poseDTO);
    }

    private List<JointPositionDTO> toJointPositionDTOs(AttributeValue.JointPositions jointPositions) {
        List<JointPositionDTO> jointPositionDTOS = new ArrayList<>();
        for(int i = 0; i < 6; i++){
           jointPositionDTOS.add(new JointPositionDTO(RoboJoint.valueOf("AXIS_" + (i+1)), jointPositions.jointPositions().get(i)));
        }
        return jointPositionDTOS;
    }
}
