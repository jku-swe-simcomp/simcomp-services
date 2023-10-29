package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.ExecutionResultDTO;

public interface ExecutionCommandVisitor<T,P> {
    default T visit(ExecutionCommand.AdjustJointAnglesCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.CalibrateCommand command, P param) throws Exception{;
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.GrabCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.OpenHandCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.PauseCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.PoseCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.ResetToHomeCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.ResumeCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.SetJointPositionsCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.SetOrientationCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.SetPositionCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.SetSpeedCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.StopCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.ToggleGripperModeCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    default T visit(ExecutionCommand.CompositeCommand command, P param) throws Exception{
        throw getUnsupportedOperationException(command);
    }

    private static UnsupportedOperationException getUnsupportedOperationException(ExecutionCommand command) {
        return new UnsupportedOperationException("The action type %s is not supported by this service.".formatted(command.getCorrespondingActionType()));
    }
}
