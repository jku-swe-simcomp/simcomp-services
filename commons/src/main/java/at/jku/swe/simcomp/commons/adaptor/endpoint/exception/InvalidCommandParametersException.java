package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

public class InvalidCommandParametersException extends RuntimeException{
    public InvalidCommandParametersException(String message) {
        super(message);
    }
}
