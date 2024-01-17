package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

/**
 * This exception is thrown when the session is not valid.
 */
public class SessionNotValidException extends Exception{
    public SessionNotValidException(String message) {
        super(message);
    }
}
