package at.jku.swe.simcomp.manager.rest.exception;

import lombok.Getter;

@Getter
public class CommandExecutionFailedException extends Exception{
    private final Integer code;
    public CommandExecutionFailedException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}
