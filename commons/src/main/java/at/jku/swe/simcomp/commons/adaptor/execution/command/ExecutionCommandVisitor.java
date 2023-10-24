package at.jku.swe.simcomp.commons.adaptor.execution.command;
import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;

import java.util.List;

public interface ExecutionCommandVisitor {
    default ExecutionResultDTO visit(ExecutionCommand.AdjustJointAnglesCommand command, String sessionKey) throws Exception {
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.ADJUST_JOINT_ANGLES));
    }

    default ExecutionResultDTO visit(ExecutionCommand.CalibrateCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.CALIBRATE));
    }

    default ExecutionResultDTO visit(ExecutionCommand.GrabCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.GRAB));
    }
    default ExecutionResultDTO visit(ExecutionCommand.OpenHandCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.OPEN_HAND));
    }
    default ExecutionResultDTO visit(ExecutionCommand.PauseCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.PAUSE));
    }
    default ExecutionResultDTO visit(ExecutionCommand.PoseCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.POSE));
    }
    default ExecutionResultDTO visit(ExecutionCommand.ResetToHomeCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.RESET_TO_HOME));
    }
    default ExecutionResultDTO visit(ExecutionCommand.ResumeCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.RESUME));
    }
    default ExecutionResultDTO visit(ExecutionCommand.SetJointPositionsCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.SET_JOINT_POSITIONS));
    }
    default ExecutionResultDTO visit(ExecutionCommand.SetOrientationCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.SET_ORIENTATION));
    }
    default ExecutionResultDTO visit(ExecutionCommand.SetPositionCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.SET_POSITION));
    }
    default ExecutionResultDTO visit(ExecutionCommand.SetSpeedCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.SET_SPEED));
    }
    default ExecutionResultDTO visit(ExecutionCommand.StopCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.STOP));
    }
    default ExecutionResultDTO visit(ExecutionCommand.ToggleGripperModeCommand command, String sessionKey) throws Exception{
        throw new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(ActionType.TOGGLE_GRIPPER_MODE));
    }

    default ExecutionResultDTO visitMultiple(List<ExecutionCommand> executionCommands, String sessionKey) throws Exception{
        ExecutionResultDTO resultDTO = null;
        for(var command: executionCommands){
            resultDTO = command.accept(this, sessionKey);
        }
        return resultDTO;
    }
}
