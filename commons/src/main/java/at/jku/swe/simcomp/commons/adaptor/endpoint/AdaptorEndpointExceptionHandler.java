package at.jku.swe.simcomp.commons.adaptor.endpoint;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.*;
import at.jku.swe.simcomp.commons.adaptor.registration.exception.ServiceRegistrationFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class AdaptorEndpointExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleSessionNotValidException(SessionNotValidException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(401)
                .message("Session not valid with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(400).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleBadRequest(BadRequestException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(401)
                .message("Bad Request: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(400).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleInvalidCommandParametersException(InvalidCommandParametersException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(400)
                .message("Invalid command parameters with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(400).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleSessionInitializationFailedException(SessionInitializationFailedException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(500)
                .message("Session initialization failed with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(500).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleServiceRegistrationFailedException(ServiceRegistrationFailedException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(500)
                .message("Could not (un-)register at service registry with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(500).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleUnsupportedOperation(UnsupportedOperationException e){
        HttpErrorDTO errorDTO = HttpErrorDTO.builder()
                .status(501)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(501).body(errorDTO);
    }
    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleJsonProcessingException(JsonProcessingException e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(500)
                .message("Error while processing JSON with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(500).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleRoboOperationFailed(RoboOperationFailedException e){
        ExecutionErrorDTO result = new ExecutionErrorDTO();
        result.setMessage(e.getMessage());
        result.setStatus(500);
        result.setState(e.getState());
        return ResponseEntity.status(500).body(result);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleException(Exception e){
        HttpErrorDTO result = HttpErrorDTO.builder()
                .status(500)
                .message("Unexpected error with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.status(500).body(result);
    }
}
