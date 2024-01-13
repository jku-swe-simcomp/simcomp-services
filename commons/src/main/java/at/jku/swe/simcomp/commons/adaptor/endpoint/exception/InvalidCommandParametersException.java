package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

import java.security.InvalidParameterException;
/**
 * This exception is thrown when the parameters of a command are not valid.
 */
public class InvalidCommandParametersException extends InvalidParameterException {
    public InvalidCommandParametersException(String message) {
        super(message);
    }
}
