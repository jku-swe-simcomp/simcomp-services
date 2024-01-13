package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception;

/**
 * This exception is thrown when a command could not be validated.
 */
public class CommandValidationFailedException extends RuntimeException{
    public CommandValidationFailedException(String message) {
        super(message);
    }
}
