package at.jku.swe.simcomp.serviceregistry.rest.exceptions;

public class AdaptorAlreadyRegisteredException extends Exception{
   public AdaptorAlreadyRegisteredException(String message){
       super(message);
   }
}
