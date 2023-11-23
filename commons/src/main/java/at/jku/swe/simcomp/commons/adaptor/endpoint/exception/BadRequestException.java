package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
