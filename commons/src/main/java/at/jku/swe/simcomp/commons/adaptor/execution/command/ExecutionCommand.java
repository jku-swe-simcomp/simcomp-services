package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.*;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.InvalidCommandParametersException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.CompositeCommandExecutionFailedException;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

import java.lang.reflect.Constructor;
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
        @JsonSubTypes.Type(value = ExecutionCommand.AdjustJointAnglesCommand.class, name = "ADJUST_JOINT_ANGLES"),
        @JsonSubTypes.Type(value = ExecutionCommand.CalibrateCommand.class, name = "CALIBRATE"),
        @JsonSubTypes.Type(value = ExecutionCommand.GrabCommand.class, name = "GRAB"),
        @JsonSubTypes.Type(value = ExecutionCommand.OpenHandCommand.class, name = "OPEN_HAND"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetJointPositionsCommand.class, name = "SET_JOINT_POSITIONS"),
        @JsonSubTypes.Type(value = ExecutionCommand.ResetToHomeCommand.class, name = "RESET_TO_HOME"),
        @JsonSubTypes.Type(value = ExecutionCommand.ToggleGripperModeCommand.class, name = "TOGGLE_GRIPPER_MODE"),
        @JsonSubTypes.Type(value = ExecutionCommand.SetSpeedCommand.class, name = "SET_SPEED"),
        @JsonSubTypes.Type(value = ExecutionCommand.CompositeCommand.class, name = "COMPOSITE"),
})
public interface ExecutionCommand {
    ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception;
    public static record PoseCommand(@NonNull PositionDTO position, @NonNull QuaternionDTO orientation) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }

    public static record SetPositionCommand(@NonNull PositionDTO position) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record SetOrientationCommand(@NonNull QuaternionDTO orientation) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record AdjustJointAnglesCommand(@NonNull List<JointAngleAdjustmentDTO> jointAngleAdjustments) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record SetJointPositionsCommand(@NonNull List<JointPositionDTO> jointPosition) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record SetSpeedCommand(@NonNull Double speed) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }

    public static record PauseCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record ResumeCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }

    }
    public static record ResetToHomeCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record StopCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record CalibrateCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record ToggleGripperModeCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record GrabCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }
    public static record OpenHandCommand() implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            return visitor.visit(this, sessionKey);
        }
    }

    public static record CompositeCommand(List<ExecutionCommand> commands) implements ExecutionCommand {
        @Override
        public ExecutionResultDTO accept(ExecutionCommandVisitor visitor, String sessionKey) throws Exception {
            if(commands.isEmpty()){
                throw new InvalidCommandParametersException("The composite command must contain at least one command.");
            }
            ExecutionResultDTO resultDTO = null;
            StringBuilder report = new StringBuilder();

            for(var command: commands){
                resultDTO = tryAcceptSubCommand(command, visitor, sessionKey, report);
                report.append(resultDTO.getReport()).append(" \n");
            }
            return setMessageAndReturn(resultDTO, report.toString());
        }

        private ExecutionResultDTO tryAcceptSubCommand(ExecutionCommand command, ExecutionCommandVisitor visitor, String sessionKey, StringBuilder report) throws CompositeCommandExecutionFailedException {
            try {
                return command.accept(visitor, sessionKey);
            } catch(CompositeCommandExecutionFailedException e){// a nested composite-command threw the exception, not inserting the prefix to the report
                report.insert(0, e.getMessage());
                throw new CompositeCommandExecutionFailedException(report.toString(), e.getOriginalException());
            } catch (Exception e) {// a scalar operation threw the exception, inserting prefix for composite commands
                report.insert(0, "The execution of the composite command threw an exception with message: "
                        + e.getMessage()
                        + "\n"
                        + "Execution reports up to this point were: \n");
                throw new CompositeCommandExecutionFailedException(report.toString(), e);
            }
        }

        private ExecutionResultDTO setMessageAndReturn(ExecutionResultDTO resultDTO, String message) {
            resultDTO.setReport(message);
            return resultDTO;
        }
    }
}
