package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.endpoint.exception.BadRequestException;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionDTO;
import at.jku.swe.simcomp.manager.service.ExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/session")
@Slf4j
@Tag(description = "Endpoints to execute commands on simulation instances associated with a session.", name = "Execution Controller")
public class ExecutionController {
    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @Operation(summary = "Execute Command",
            description = "Asynchronously executes the command against all simulation instances associated with the session." +
                    "If a command is not supported by one of the simulation types associated with the session, the command is not distributed to the corresponding instance." +
                    "Returns the ID of the request as UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202",
                    description = "Commands distributed.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = UUID.class),
                            examples = @ExampleObject(value = "123e4567-e89b-12d3-a456-426614174001")
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"Session not found.\"}")
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
                            @ExampleObject(name = "Composite Command", value = "{ \"type\": \"COMPOSITE\", \"commands\":[{\"type\": \"SET_JOINT_POSITION\", \"jointPosition\": {\"joint\":\"AXIS_1\", \"radians\":1.0}},{\"type\": \"GRAB\"}]}"),
                            @ExampleObject(name = "Adjust Joint Angle", value = "{ \"type\": \"ADJUST_JOINT_ANGLE\", \"jointAngleAdjustment\": {\"joint\":\"AXIS_1\", \"byRadians\":1.0}}"),
                            @ExampleObject(name = "Set Joint Position", value = "{ \"type\": \"SET_JOINT_POSITION\", \"jointPosition\": {\"joint\":\"AXIS_1\", \"radians\":1.0}}"),
                            @ExampleObject(name = "Pose Command", value = "{ \"type\": \"POSE\", \"position\": {\"x\": 0.0, \"y\":0.0, \"z\":1.0}, \"orientation\": {\"x\": 0.0, \"y\":0.0, \"z\":1.0}}"),
                            @ExampleObject(name = "Set Position", value = "{ \"type\": \"SET_POSITION\", \"position\": {\"x\": 0.0, \"y\":0.0, \"z\":1.0}}"),
                            @ExampleObject(name = "Set Orientation", value = "{ \"type\": \"SET_ORIENTATION\", \"orientation\": {\"x\": 0.0, \"y\":0.0, \"z\":1.0}}"),
                            @ExampleObject(name = "Reset to Home", value = "{ \"type\": \"RESET_TO_HOME\"}"),
                            @ExampleObject(name = "Grab", value = "{ \"type\": \"GRAB\"}"),
                            @ExampleObject(name = "Toggle Gripper Mode", value = "{ \"type\": \"TOGGLE_GRIPPER_MODE\"}"),
                            @ExampleObject(name = "Stop", value = "{ \"type\": \"STOP\"}"),
                            @ExampleObject(name = "Pause", value = "{ \"type\": \"PAUSE\"}"),
                            @ExampleObject(name = "Resume", value = "{ \"type\": \"RESUME\"}"),
                            @ExampleObject(name = "Calibrate", value = "{ \"type\": \"CALIBRATE\"}"),
                            @ExampleObject(name = "Open Hand", value = "{ \"type\": \"OPEN_HAND\"}"),
                            @ExampleObject(name = "Set Speed", value = "{ \"type\": \"SET_SPEED\", \"speed\":2.3}"),
                    }))
    @PostMapping(value = "/{sessionId}/execution", consumes = "application/json", produces = "text/html")
    public ResponseEntity<String> execute(@Parameter(description = "The id of the session.", required = true, schema = @Schema(implementation = UUID.class, example = "123e4567-e89b-12d3-a456-426614174001"))
                                              @PathVariable("sessionId") UUID sessionId,
                                  @RequestBody ExecutionCommand command) throws BadRequestException {
        log.info("Request to execute command for session {}: {}", sessionId, command);
        UUID executionId = executionService.executeCommand(sessionId, command);
        log.info("Returning execution id {}", executionId);
        return ResponseEntity.status(202).body(executionId.toString());
    }

    @Operation(summary = "Get all Executions for Session",
            description = "Returns all executions and the responses from the simulations for a given session, identified by the session-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ok.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            array = @ArraySchema(schema = @Schema(implementation = ExecutionDTO.class)),
                            examples = @ExampleObject(value = " " +
                                    "[{ \"id\": \"123e4567-e89b-12d3-a456-426614174001\", \"sessionId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"command\": {\"type\":\"GRAB\"}, \"createdAt\": \"2023-01-01T12:34:56\", " +
                                    "\"responses\": [" +
                                    "{ \"simulationName\": \"WEBOTS\", \"responseCode\": 200, \"state\":\"SUCCESS\", \"report\":\"Grab command was executed.\" }, " +
                                    "{ \"simulationName\": \"GAZEBO\", \"responseCode\": 202, \"state\":\"RUNNING\", \"report\":\"\" }, " +
                                    "{ \"simulationName\": \"PHYSICAL\", \"responseCode\": 401, \"state\":\"ERROR\", \"report\":\"Session is not valid anymore.\" } " +
                                    "]}]")
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping(value = "/{sessionId}/execution")
    public ResponseEntity<List<ExecutionDTO>> getAllExecutions(@Parameter(description = "The id of the session.", required = true, schema = @Schema(implementation = UUID.class, example = "123e4567-e89b-12d3-a456-426614174001"))
                                          @PathVariable("sessionId") UUID sessionId){
        log.info("Request to get executions for session {}.", sessionId);
        List<ExecutionDTO> executions = executionService.getAllExecutionsForSession(sessionId);
        log.info("Returning executions {}.", executions);
        return ResponseEntity.status(200).body(executions);
    }

    @Operation(summary = "Get information about execution and responses.",
            description = "Returns information about the execution and the current state of the responses from the simulation instances.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Success.",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = ExecutionDTO.class),
                            examples =
                            @ExampleObject(name = "executionDTOExample", value =
                            "{ \"id\": \"123e4567-e89b-12d3-a456-426614174001\", \"sessionId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"command\": {\"type\":\"GRAB\"}, \"createdAt\": \"2023-01-01T12:34:56\", " +
                                    "\"responses\": [" +
                                    "  { \"simulationName\": \"WEBOTS\", \"responseCode\": 200, \"state\":\"SUCCESS\", \"report\":\"Grab command was executed.\" }, " +
                                    "  { \"simulationName\": \"GAZEBO\", \"responseCode\": 202, \"state\":\"RUNNING\", \"report\":\"\" }, " +
                                    "  { \"simulationName\": \"PHYSICAL\", \"responseCode\": 401, \"state\":\"ERROR\", \"report\":\"Session is not valid anymore.\" } " +
                                    "] }"
                    ))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Session not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"Session not found.\"}")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping(value = "/execution/{executionId}", produces = "application/json")
    public ResponseEntity<ExecutionDTO> getExecutionDetails(@Parameter(description = "The id of the execution", schema = @Schema(implementation = UUID.class))
                                                                @PathVariable UUID executionId){
        log.info("Request to fetch execution {}.", executionId);
        ExecutionDTO dto = executionService.getExecution(executionId);
        return ResponseEntity.ok(dto);
    }
}
