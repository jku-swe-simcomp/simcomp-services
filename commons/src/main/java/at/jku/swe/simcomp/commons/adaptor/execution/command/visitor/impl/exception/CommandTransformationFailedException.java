package at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.exception;

public class CommandTransformationFailedException extends RuntimeException{
    public CommandTransformationFailedException(String message) {
        super(message);
    }
}
