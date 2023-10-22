package at.jku.swe.simcomp.commons.adaptor.dto;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.InvalidCommandParametersException;
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
    private Double speed;

    public PoseCommand viewAsPoseCommand(){
       return new PoseCommand(PoseDTO.builder()
                          .position(position)
                          .quaternion(orientation)
                          .build());
    }

    public AdjustJointAngleCommand viewAsAdjustJointAngleCommand(){
        try{
            return new AdjustJointAngleCommand(jointAdjustments);
        } catch (NullPointerException e){
            throw new InvalidCommandParametersException("Joint adjustments are required for action type %s".formatted(actionType));
        }
    }

    public SetJointPositionCommand viewAsSetJointPositionCommand(){
        try{
            return new SetJointPositionCommand(jointPositions);
        } catch (NullPointerException e){
            throw new InvalidCommandParametersException("Joint positions are required for action type %s".formatted(actionType));
        }
    }

    public SetSpeedCommand viewAsSetSpeedCommand(){
        try{
            return new SetSpeedCommand(speed);
        } catch (NullPointerException e){
            throw new InvalidCommandParametersException("Speed is required for action type %s".formatted(actionType));
        }
    }

    public SetOrientationCommand viewAsSetOrientationCommand(){
        try{
            return new SetOrientationCommand(orientation);
        } catch (NullPointerException e){
            throw new InvalidCommandParametersException("Orientation is required for action type %s".formatted(actionType));
        }
    }

    public SetPositionCommand viewAsSetPositionCommand(){
        try{
            return new SetPositionCommand(position);
        } catch (NullPointerException e){
            throw new InvalidCommandParametersException("Position is required for action type %s".formatted(actionType));
        }
    }
}
