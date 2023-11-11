package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionResponseDTO;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionStateDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.UUID;

@RestController
@RequestMapping("/session")
@Slf4j
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/init")
    public ResponseEntity<SessionResponseDTO> createSession(@RequestBody SessionRequest request) throws SessionInitializationFailedException {
        log.info("Initialize Session Request: {}", request);
        Session session = (Session) request.accept(sessionService);
        log.info("Initialized session: {}", session);
        SessionResponseDTO response = new SessionResponseDTO(session.getSessionKey().toString(),
                session.getAdaptorSessions().stream().map(AdaptorSession::getAdaptorName).toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sessionKey}")
    public ResponseEntity<SessionStateDTO> getSessionState(@PathVariable UUID sessionKey){
        log.info("Get Session State Request: {}", sessionKey);
        SessionStateDTO state = sessionService.getSessionState(sessionKey);
        log.info("Session state: {}",state);
        return ResponseEntity.ok(state);
    }

    @DeleteMapping("/{sessionKey}")
    public ResponseEntity<Void> closeSession(@PathVariable UUID sessionKey){
        log.info("Close Session Request: {}", sessionKey);
        sessionService.closeSession(sessionKey);
        return ResponseEntity.ok().build();
    }

    // exception handlers
    @ExceptionHandler
    public ResponseEntity<HttpErrorDTO> handleSessionInitializationFailed(SessionInitializationFailedException e){
        HttpErrorDTO dto = HttpErrorDTO.builder()
                .status(500)
                .message("Could not initialize session with message: %s".formatted(e.getMessage()))
                .build();
        return ResponseEntity.internalServerError().body(dto);
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleNotFound(NotFoundException e){
        return ResponseEntity.notFound().build();
    }
}
