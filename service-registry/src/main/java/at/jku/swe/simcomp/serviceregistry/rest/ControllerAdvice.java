package at.jku.swe.simcomp.serviceregistry.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.serviceregistry.rest.exceptions.AdaptorAlreadyRegisteredException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles errors thrown in the rest-endpoints.
 */
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    /**
     * Handles the given exception.
     * @param e the exception
     * @return a dto with information about the exception.
     */
    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleAdaptorAlreadyRegistered(AdaptorAlreadyRegisteredException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(400).body(result);
    }
}
