package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception;

public class CommandValidationFailedException extends RuntimeException{
    public CommandValidationFailedException(String message) {
        super(message);
    }
}
