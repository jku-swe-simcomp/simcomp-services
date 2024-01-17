package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception;

/**
 * This exception is thrown when a command could not be transformed into a simulation specific command.
 */
public class CommandTransformationFailedException extends RuntimeException{
    public CommandTransformationFailedException(String message) {
        super(message);
    }
}
