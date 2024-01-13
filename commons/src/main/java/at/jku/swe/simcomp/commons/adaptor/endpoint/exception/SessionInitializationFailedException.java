package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

/**
 * This exception is thrown when the session initialization failed.
 */
public class SessionInitializationFailedException extends Exception{
    public SessionInitializationFailedException(String message) {
        super(message);
    }
}
