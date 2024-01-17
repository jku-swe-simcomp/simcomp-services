package at.jku.swe.simcomp.serviceregistry.rest.exceptions;

/**
 * Exception thrown when an adaptor is already registered.
 */
public class AdaptorAlreadyRegisteredException extends Exception{
   public AdaptorAlreadyRegisteredException(String message){
       super(message);
   }
}
