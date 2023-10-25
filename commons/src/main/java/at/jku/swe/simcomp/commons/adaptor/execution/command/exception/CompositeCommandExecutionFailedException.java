package at.jku.swe.simcomp.commons.adaptor.execution.command.exception;

public class CompositeCommandExecutionFailedException extends Exception {
    public CompositeCommandExecutionFailedException(String message) {
        super(message);
    }
}
