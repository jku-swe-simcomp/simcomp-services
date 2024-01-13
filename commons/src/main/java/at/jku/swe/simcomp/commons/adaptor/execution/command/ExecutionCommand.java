package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.*;
import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.ExecutionCommandVisitor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.util.List;

/**
 * This interface represents a command to be executed.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExecutionCommand.PoseCommand.class, name = "POSE"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetPositionCommand.class, name = "SET_POSITION"),
        @JsonSubTypes.Type(value = ExecutionCommand.ResumeCommand.class, name = "RESUME"),
        @JsonSubTypes.Type(value = ExecutionCommand.StopCommand.class, name = "STOP"),
        @JsonSubTypes.Type(value = ExecutionCommand.PauseCommand.class, name = "PAUSE"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetOrientationCommand.class, name = "SET_ORIENTATION"),
        @JsonSubTypes.Type(value = ExecutionCommand.AdjustJointAngleCommand.class, name = "ADJUST_JOINT_ANGLE"),
        @JsonSubTypes.Type(value = ExecutionCommand.CalibrateCommand.class, name = "CALIBRATE"),
        @JsonSubTypes.Type(value = ExecutionCommand.GrabCommand.class, name = "GRAB"),
        @JsonSubTypes.Type(value = ExecutionCommand.OpenHandCommand.class, name = "OPEN_HAND"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetJointPositionCommand.class, name = "SET_JOINT_POSITION"),
        @JsonSubTypes.Type(value = ExecutionCommand.ResetToHomeCommand.class, name = "RESET_TO_HOME"),
        @JsonSubTypes.Type(value = ExecutionCommand.ToggleGripperModeCommand.class, name = "TOGGLE_GRIPPER_MODE"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetSpeedCommand.class, name = "SET_SPEED"),
        @JsonSubTypes.Type(value = ExecutionCommand.CompositeCommand.class, name = "COMPOSITE"),
})
@Schema(description = "A command to be executed.")
public interface ExecutionCommand {
    /**
     * Accepts a visitor to perform some operation on the command.
     * @param visitor the visitor
     * @param param additional parameter for the visitor
     * @return the result of the visit
     * @param <T> the result type
     * @param <P> the type of the additional parameter
     * @throws Exception if the visit fails
     */
    <T,P> T accept(ExecutionCommandVisitor<T,P> visitor, P param) throws Exception;

    /**
     * Returns the corresponding action type {@link ActionType} of the command.
     * @return the corresponding action type
     */
    @JsonIgnore
    ActionType getCorrespondingActionType();

    /**
     * This record represents a pose command.
     * @param position the position
     * @param orientation the orientation
     */
    record PoseCommand(@NonNull PositionDTO position, @NonNull OrientationDTO orientation) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.POSE;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a set position command.
     * @param position the position
     */
    record SetPositionCommand(@NonNull PositionDTO position) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.SET_POSITION;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a set orientation command.
     * @param orientation the orientation
     */
    record SetOrientationCommand(@NonNull OrientationDTO orientation) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.SET_ORIENTATION;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }
    /**
     * This record represents a adjust joint angle command.
     * @param jointAngleAdjustment the joint angle adjustment
     */
    record AdjustJointAngleCommand(@NonNull JointAngleAdjustmentDTO jointAngleAdjustment) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.ADJUST_JOINT_ANGLE;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a set joint position command.
     * @param jointPosition the joint position
     */
    record SetJointPositionCommand(@NonNull JointPositionDTO jointPosition) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.SET_JOINT_POSITION;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a set speed command.
     * @param speed the speed
     */
    record SetSpeedCommand(@NonNull Double speed) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.SET_SPEED;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a pause command.
     */
    record PauseCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.PAUSE;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a resume command.
     */
    record ResumeCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.RESUME;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }

    }

    /**
     * This record represents a reset to home command.
     */
    record ResetToHomeCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.RESET_TO_HOME;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a stop command.
     */
    record StopCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.STOP;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a calibrate command.
     */
    record CalibrateCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.CALIBRATE;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a toggle gripper mode command.
     */
    record ToggleGripperModeCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.TOGGLE_GRIPPER_MODE;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a grab command.
     */
    record GrabCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.GRAB;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents an open hand command.
     */
    record OpenHandCommand() implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.OPEN_HAND;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }

    /**
     * This record represents a composite command.
     * @param commands the commands to execute
     */
    record CompositeCommand(List<? extends ExecutionCommand> commands) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.COMPOSITE;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }
}
