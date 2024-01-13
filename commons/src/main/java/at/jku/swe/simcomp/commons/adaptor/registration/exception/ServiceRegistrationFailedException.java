package at.jku.swe.simcomp.commons.adaptor.registration.exception;

/**
 * This exception is thrown when a service registration failed.
 */
public class ServiceRegistrationFailedException extends Exception{
    public ServiceRegistrationFailedException(String message){
        super(message);
    }
}
