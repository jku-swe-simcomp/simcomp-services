package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

import java.security.InvalidParameterException;

public class InvalidCommandParametersException extends InvalidParameterException {
    public InvalidCommandParametersException(String message) {
        super(message);
    }
}
