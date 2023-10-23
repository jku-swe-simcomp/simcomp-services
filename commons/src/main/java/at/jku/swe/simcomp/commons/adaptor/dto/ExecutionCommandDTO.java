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
}
