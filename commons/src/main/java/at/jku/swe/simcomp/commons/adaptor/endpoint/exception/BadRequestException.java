package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

/**
 * This exception is thrown when the request is not valid.
 */
public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
