package at.jku.swe.simcomp.commons.adaptor.dto;

import at.jku.swe.simcomp.commons.adaptor.execution.command.*;
import lombok.*;

import java.util.List;

/**
 * A rich DTO which contains data required for the execution
 * of an action defined by the {@link ActionType}.
 *
 * Not all fields might be required for a given action, e.g. {@link ActionType#STOP}
 * would require no additional data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionCommandDTO implements ExecutionCommand {
    @NonNull
    private ActionType actionType;
    /**
     * Required for action types...
     */
    private PositionDTO position;
    private QuaternionDTO orientation;
    private List<JointAngleAdjustmentDTO> jointAdjustments;
    private List<JointPositionDTO> jointPositions;
    private double speed;

    public PoseCommand viewAsPoseCommand(){
       return new PoseCommand(PoseDTO.builder()
                          .position(position)
                          .quaternion(orientation)
                          .build());
    }

    public AdjustJointAngleCommand viewAsAdjustJointAngleCommand(){
        return new AdjustJointAngleCommand(jointAdjustments);
    }

    public SetJointPositionCommand viewAsSetJointPositionCommand(){
        return new SetJointPositionCommand(jointPositions);
    }

    public SetSpeedCommand viewAsSetSpeedCommand(){
        return new SetSpeedCommand(speed);
    }

    public SetOrientationCommand viewAsSetOrientationCommand(){
        return new SetOrientationCommand(orientation);
    }

    public SetPositionCommand viewAsSetPositionCommand(){
        return new SetPositionCommand(position);
    }
}
