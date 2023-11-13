package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.manager.rest.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleNotFound(NotFoundException e){
        HttpErrorDTO dto = HttpErrorDTO.builder()
                .status(404)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(404).body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleBadRequest(BadRequestException e){
        HttpErrorDTO dto = HttpErrorDTO.builder()
                .status(400)
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(400).body(dto);
    }
}
