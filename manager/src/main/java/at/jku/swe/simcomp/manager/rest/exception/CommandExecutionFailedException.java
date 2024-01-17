package at.jku.swe.simcomp.manager.rest.exception;

import lombok.Getter;

/**
 * Exception that is thrown when a command execution failed.
 * Contains the error code that was returned by the execution.
 */
@Getter
public class CommandExecutionFailedException extends Exception{
    private final Integer code;
    public CommandExecutionFailedException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
