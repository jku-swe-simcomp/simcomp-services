package at.jku.swe.simcomp.commons.adaptor.dto;

import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.InvalidCommandParametersException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.*;
import lombok.*;

import java.util.List;
import java.util.function.Supplier;

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
        return tryViewAsExecutionCommand(() -> new PoseCommand(position, orientation));
    }

    public AdjustJointAnglesCommand viewAsAdjustJointAngleCommand(){
        return tryViewAsExecutionCommand(() -> new AdjustJointAnglesCommand(jointAdjustments));
    }

    public SetJointPositionsCommand viewAsSetJointPositionCommand(){
        return tryViewAsExecutionCommand(() -> new SetJointPositionsCommand(jointPositions));
    }

    public SetSpeedCommand viewAsSetSpeedCommand(){
        return tryViewAsExecutionCommand(() -> new SetSpeedCommand(speed));
    }

    public SetOrientationCommand viewAsSetOrientationCommand(){
        return tryViewAsExecutionCommand(() -> new SetOrientationCommand(orientation));
    }

    public SetPositionCommand viewAsSetPositionCommand(){
       return tryViewAsExecutionCommand(() -> new SetPositionCommand(position));
    }

    public StopCommand viewAsStopCommand(){
        return tryViewAsExecutionCommand(StopCommand::new);
    }
    public SetJointPositionsCommand viewAsSetJointAnglesCommand(){
        return tryViewAsExecutionCommand(() -> new SetJointPositionsCommand(jointPositions));
    }

    public PauseCommand viewAsPauseCommand(){
        return tryViewAsExecutionCommand(PauseCommand::new);
    }

    public CalibrateCommand viewAsCalibrateCommand(){
        return tryViewAsExecutionCommand(CalibrateCommand::new);
    }

    public ToggleGripperModeCommand viewAsToggleGripperModeCommand(){
        return tryViewAsExecutionCommand(ToggleGripperModeCommand::new);
    }

    public ResumeCommand viewAsResumeCommand(){
        return tryViewAsExecutionCommand(ResumeCommand::new);
    }

    public ResetToHomeCommand viewAsResetToHomeCommand(){
        return tryViewAsExecutionCommand(ResetToHomeCommand::new);
    }

    public OpenHandCommand viewAsOpenHandCommand(){
        return tryViewAsExecutionCommand(OpenHandCommand::new);
    }

    public GrabCommand viewAsGrabCommand(){
        return tryViewAsExecutionCommand(GrabCommand::new);
    }

    private <T extends ExecutionCommand> T tryViewAsExecutionCommand(Supplier<T> supplier){
        try {
            return supplier.get();
        } catch (NullPointerException e){
            throw new InvalidCommandParametersException("Required parameters are missing for action type %s".formatted(actionType));
        }
    }

    @Override
    public ExecutionResultDTO accept(CommandExecutionVisitor visitor, String sessionKey) throws Exception {
        return switch(actionType){
            case POSE -> viewAsPoseCommand().accept(visitor, sessionKey);
            case SET_POSITION -> viewAsSetPositionCommand().accept(visitor, sessionKey);
            case SET_ORIENTATION -> viewAsSetOrientationCommand().accept(visitor, sessionKey);
            case GRAB -> viewAsGrabCommand().accept(visitor, sessionKey) ;
            case OPEN_HAND -> viewAsOpenHandCommand().accept(visitor, sessionKey);
            case ADJUST_JOINT_ANGLES -> viewAsAdjustJointAngleCommand().accept(visitor, sessionKey);
            case SET_JOINT_POSITIONS -> viewAsSetJointPositionCommand().accept(visitor, sessionKey);
            case SET_SPEED -> viewAsSetSpeedCommand().accept(visitor, sessionKey);
            case PAUSE -> viewAsPauseCommand().accept(visitor, sessionKey);
            case RESUME -> viewAsResumeCommand().accept(visitor, sessionKey);
            case RESET_TO_HOME -> viewAsResetToHomeCommand().accept(visitor, sessionKey);
            case STOP -> viewAsStopCommand().accept(visitor, sessionKey);
            case CALIBRATE -> viewAsCalibrateCommand().accept(visitor, sessionKey);
            case TOGGLE_GRIPPER_MODE -> viewAsToggleGripperModeCommand().accept(visitor, sessionKey);
            case COMPOSITE -> null;
        };
    }

    public ExecutionCommand viewAsExecutionCommand(){
        return switch(actionType){
            case POSE -> viewAsPoseCommand();
            case SET_POSITION -> viewAsSetPositionCommand();
            case SET_ORIENTATION -> viewAsSetOrientationCommand();
            case GRAB -> viewAsGrabCommand() ;
            case OPEN_HAND -> viewAsOpenHandCommand();
            case ADJUST_JOINT_ANGLES -> viewAsAdjustJointAngleCommand();
            case SET_JOINT_POSITIONS -> viewAsSetJointPositionCommand();
            case SET_SPEED -> viewAsSetSpeedCommand();
            case PAUSE -> viewAsPauseCommand();
            case RESUME -> viewAsResumeCommand();
            case RESET_TO_HOME -> viewAsResetToHomeCommand();
            case STOP -> viewAsStopCommand();
            case CALIBRATE -> viewAsCalibrateCommand();
            case TOGGLE_GRIPPER_MODE -> viewAsToggleGripperModeCommand();
            case COMPOSITE -> null;
        };
    }
}
