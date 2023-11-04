package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

import java.util.List;

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
        @JsonSubTypes.Type(value = ExecutionCommand.SetJointPositionCommand.class, name = "SET_JOINT_POSITIONS"),
        @JsonSubTypes.Type(value = ExecutionCommand.ResetToHomeCommand.class, name = "RESET_TO_HOME"),
        @JsonSubTypes.Type(value = ExecutionCommand.ToggleGripperModeCommand.class, name = "TOGGLE_GRIPPER_MODE"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetSpeedCommand.class, name = "SET_SPEED"),
        @JsonSubTypes.Type(value = ExecutionCommand.CompositeCommand.class, name = "COMPOSITE"),
})
public interface ExecutionCommand {
    <T,P> T accept(ExecutionCommandVisitor<T,P> visitor, P param) throws Exception;
    ActionType getCorrespondingActionType();
    public static record PoseCommand(@NonNull PositionDTO position, @NonNull QuaternionDTO orientation) implements ExecutionCommand {
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

    public static record SetPositionCommand(@NonNull PositionDTO position) implements ExecutionCommand {
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
    public static record SetOrientationCommand(@NonNull QuaternionDTO orientation) implements ExecutionCommand {
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
    public static record AdjustJointAngleCommand(@NonNull JointAngleAdjustmentDTO jointAngleAdjustment) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.ADJUST_JOINT_ANGLES;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }
    public static record SetJointPositionCommand(@NonNull JointPositionDTO jointPosition) implements ExecutionCommand {
        private static final ActionType correspondingActionType = ActionType.SET_JOINT_POSITIONS;

        @Override
        public <T,P> T accept(ExecutionCommandVisitor<T, P> visitor, P param) throws Exception {
            return visitor.visit(this, param);
        }

        @Override
        public ActionType getCorrespondingActionType() {
            return correspondingActionType;
        }
    }
    public static record SetSpeedCommand(@NonNull Double speed) implements ExecutionCommand {
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

    public static record PauseCommand() implements ExecutionCommand {
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
    public static record ResumeCommand() implements ExecutionCommand {
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
    public static record ResetToHomeCommand() implements ExecutionCommand {
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
    public static record StopCommand() implements ExecutionCommand {
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
    public static record CalibrateCommand() implements ExecutionCommand {
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
    public static record ToggleGripperModeCommand() implements ExecutionCommand {
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
    public static record GrabCommand() implements ExecutionCommand {
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
    public static record OpenHandCommand() implements ExecutionCommand {
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

    public static record CompositeCommand(List<ExecutionCommand> commands) implements ExecutionCommand {
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
