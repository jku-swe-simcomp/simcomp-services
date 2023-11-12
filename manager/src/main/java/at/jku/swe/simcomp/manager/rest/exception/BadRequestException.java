package at.jku.swe.simcomp.manager.rest.exception;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
}
