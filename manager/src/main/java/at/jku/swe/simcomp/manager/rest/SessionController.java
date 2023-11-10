package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.ExecutionErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionResponseDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/session")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/init")
    public ResponseEntity<SessionResponseDTO> createSession(@RequestBody SessionRequest request) throws SessionInitializationFailedException {
        Session session = (Session) request.accept(sessionService);
        SessionResponseDTO response = new SessionResponseDTO(session.getSessionKey().toString(),
                session.getAdaptorSessions().stream().map(AdaptorSession::getAdaptorName).toList());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleSessionInitializationFailed(SessionInitializationFailedException e){
        HttpErrorDTO dto = HttpErrorDTO.builder()
                .status(500)
                .message("Could not initialize session with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.internalServerError().body(dto);
    }
}
