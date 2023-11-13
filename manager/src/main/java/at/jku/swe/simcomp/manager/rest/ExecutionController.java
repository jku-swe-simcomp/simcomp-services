package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.manager.rest.exception.BadRequestException;
import at.jku.swe.simcomp.manager.service.ExecutionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/session/{sessionId}/execution")
@Slf4j
public class ExecutionController {
    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping
    public ResponseEntity<String> execute(@PathVariable("sessionId") UUID sessionId,
                                  @RequestBody ExecutionCommand command) throws BadRequestException {
        log.info("Request to execute command for session {}: {}", sessionId, command);
        UUID executionId = executionService.executeCommand(sessionId, command);
        log.info("Returning execution id {}", executionId);
        return ResponseEntity.status(202).body(executionId.toString());
    }

}
