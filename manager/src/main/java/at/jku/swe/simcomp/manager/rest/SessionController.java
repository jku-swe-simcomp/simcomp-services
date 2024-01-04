package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.SessionInitializationFailedException;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionRequest;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionResponseDTO;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionStateDTO;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.UUID;

/**
 * REST Controller for managing sessions.
 * Refer to the OpenAPI documentation for more information.
 */
@RestController
@RequestMapping("/session")
@Slf4j
@Tag(name = "Session Controller", description = "Endpoints to manage sessions.")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Operation(summary = "Initialize a new session",
            description = "Initializes a new session. Not all requested simulation types might be available. " +
                    "An error is only thrown if no single simulation instance could be obtained, otherwise, the response indicates the obtained simulations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success (at least one of the requested simulations obtained)",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = SessionResponseDTO.class),
                            examples = @ExampleObject(value = "{\"sessionKey\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"acquiredSimulations\":[\"WEBOTS\", \"GAZEBO\"]}")
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Session initialization failed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Not a single session could be obtained.\"}")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The command to be executed",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    schema = @Schema(implementation = ExecutionCommand.class),
                    examples = {
                            @ExampleObject(name = "Any simulations", value = "{ \"type\": \"ANY\", \"n\":2}"),
                            @ExampleObject(name = "Selected Simulation Types", value = "{ \"type\": \"SELECTED_TYPES\", \"requestedSimulationTypes\": [\"WEBOTS\", \"GAZEBO\"]}"),
                            @ExampleObject(name = "Selected Simulation Instances", value = "{ \"type\": \"SELECTED_INSTANCES\", \"requestedSimulationInstances\": {\"WEBOTS\":\"MY_PERSONAL_WEBOTS_INSTANCE\", \"GAZEBO\":\"MY_PERSONAL_GAZEBO_INSTANCE\"}}"),
                    }))
    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<SessionResponseDTO> createSession(@RequestBody SessionRequest request) throws SessionInitializationFailedException {
        log.info("Initialize Session Request: {}", request);
        Session session = (Session) request.accept(sessionService);
        log.info("Initialized session: {}", session);
        SessionResponseDTO response = new SessionResponseDTO(session.getSessionKey().toString(),
                session.getAdaptorSessions().stream().map(AdaptorSession::getAdaptorName).toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add simulation to existing session.",
            description = "Try to add a simulation with the given type to an existing simulation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success (simulation with given type added to session)"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"No session with given id found.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":400, \"message\":\"Simulation instance with same type already part of session.\"}"),
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"This simulation type is not registered.\"}")
                            }
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}"),
                            }
                    ))
    })
    @PostMapping("/{sessionKey}/{simulationType}")
    public ResponseEntity<Void> addAdaptorSessionToAggregateSession(@Parameter(description = "The id of the session") @PathVariable UUID sessionKey,
                                                                    @Parameter(description = "The simulation type to add") @PathVariable String simulationType) throws BadRequestException, SessionInitializationFailedException, NotFoundException {
        log.info("Request to add {} to session {}.", simulationType, sessionKey);
        sessionService.addAdaptorSessionToAggregatedSession(sessionKey, simulationType);
        log.info("Added {}.", simulationType);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reopen a closed simulation of an existing session.",
            description = "Try to reopen a closed simulation with the given type of an existing session." +
                    "If the simulation with the given type has been initially added with a specific simulation instance, attempts to reopen the same simulation instance." +
                    "If the simulation has not been initially added with a specific simulation instance, attempts to obtain any simulation instance with the indicated type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success (simulation with given type reopened.)"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"No session with given id found.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":400, \"message\":\"Simulation instance with same type already part of session.\"}"),
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"This simulation type is not registered.\"}")
                            }
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}"),
                            }
                    ))
    })
    @PatchMapping("/{sessionKey}/{simulationType}/reopen")
    public ResponseEntity<Void> reopenAdaptorSessionOfAggregateSession(@Parameter(description = "The id of the session")
                                                                           @PathVariable UUID sessionKey,
                                                                       @Parameter(description = "The type of the simulation to reopen")
                                                                       @PathVariable String simulationType) throws BadRequestException, SessionInitializationFailedException, NotFoundException {
        log.info("Request to reopen {} from session {}.", simulationType, sessionKey);
        sessionService.reopenAdaptorSessionOfAggregateSession(sessionKey, simulationType);
        log.info("Reopened {}.", simulationType);

        return ResponseEntity.ok().build();

    }

    @Operation(summary = "Close a simulation of an existing session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success (simulation with given type closed.)"
            ),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"No session with given id found.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":400, \"message\":\"No simulation with given type associated to this session OR simulation with given type already closed.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}"),
                            }
                    ))
    })
    @PatchMapping("/{sessionKey}/{simulationType}/close")
    public ResponseEntity<Void> closeAdaptorSessionOfAggregateSession(@Parameter(description = "The id of the session")
                                                                          @PathVariable UUID sessionKey,
                                                                      @Parameter(description = "The type of the simulation to close")
                                                                      @PathVariable String simulationType) throws BadRequestException, NotFoundException {
        log.info("Request to close {} from session {}.", simulationType, sessionKey);
        sessionService.closeAdaptorSessionOfAggregateSession(sessionKey, simulationType);
        log.info("Removed {}.", simulationType);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get information about the state of the sessions and the associated simulations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SessionStateDTO.class),
                            examples = @ExampleObject(value = "{\"sessionKey\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"sessionState\":\"OPEN\", \"simulations\":{\"WEBOTS\":\"OPEN\", \"GAZEBO\":\"CLOSED\"}}")
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"No session with given id found.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}"),
                            }
                    ))
    })
    @GetMapping(value = "/{sessionKey}", produces = "application/json")
    public ResponseEntity<SessionStateDTO> getSessionState(@Parameter(description = "The id of the session")
                                                               @PathVariable UUID sessionKey) throws NotFoundException {
        log.info("Get Session State Request: {}", sessionKey);
        SessionStateDTO state = sessionService.getSessionState(sessionKey);
        log.info("Session state: {}",state);
        return ResponseEntity.ok(state);
    }

    @Operation(summary = "Close a session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success"),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"No session with given id found.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "400",
                    description = "Session already closed",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples ={
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":400, \"message\":\"Session with given id already closed.\"}"),
                            }
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples = {
                                    @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}"),
                            }
                    ))
    })
    @PatchMapping("/{sessionKey}/close")
    public ResponseEntity<Void> closeSession(@Parameter(description = "The id of the session")
                                                 @PathVariable UUID sessionKey) throws NotFoundException, BadRequestException {
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
}
