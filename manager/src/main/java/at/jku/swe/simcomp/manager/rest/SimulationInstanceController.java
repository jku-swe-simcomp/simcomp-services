package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.adaptor.endpoint.simulation.SimulationInstanceConfig;
import at.jku.swe.simcomp.commons.adaptor.execution.command.ExecutionCommand;
import at.jku.swe.simcomp.commons.manager.dto.AvailableServicesDTO;
import at.jku.swe.simcomp.manager.service.SimulationInstanceService;
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

/**
 * REST Controller for managing simulation instances.
 * Refer to the OpenAPI documentation for more information.
 */
@RestController
@RequestMapping("/simulation")
@Slf4j
@Tag(name = "Simulation Controller", description = "Endpoints for managing simulation types and corresponding simulation instances.")
public class SimulationInstanceController {
    private final SimulationInstanceService service;

    public SimulationInstanceController(SimulationInstanceService service){
        this.service = service;
    }

    @Operation(summary = "Get Simulation Types",
            description = "Returns all simulation types that are currently available and the commands/actions that are supported by them." +
                    "A simulation type is e.g. 'WEBOTS', which might provide several simulation instances of this type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Types fetched",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AvailableServicesDTO.class),
                            examples = @ExampleObject(value = "{\"availableSimulations\":[{\"name\":\"WEBOTS\", \"supportedActions\":[\"POSE\"]}]}")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping(value = "/type", produces = "application/json")
    public ResponseEntity<AvailableServicesDTO> getAvailableSimulations(){
        log.info("Request to return available simulations received.");
        var simulations = service.getAvailableSimulations();
        log.info("Available simulations: {}", simulations);
        return ResponseEntity.ok(new AvailableServicesDTO(simulations));
    }

    @Operation(summary = "Get the identifiers of the custom-commands supported by a given simulation type (e.g. WEBOTS).",
            description = "Returns a list of custom commands supported by the simulation type." +
                    "To get an example of the command, use the endpoint /type/{type}/custom-commands/{commandType}/example, were commandType is one of the supported commands.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Supported custom commands fetched",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            array = @ArraySchema(schema = @Schema(implementation = String.class)),
                            examples = @ExampleObject(value = "[\"TURN_AROUND\", \"MOVE_FORWARD\", \"MOVE_BACKWARD\", \"TURN_LEFT\", \"TURN_RIGHT\"]")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping(value="/type/{type}/custom-commands")
    public ResponseEntity<List<String>> getSupportedCustomCommands(@Parameter(description = "The simulation type (e.g. WEBOTS)", required = true)
                                                                       @PathVariable String type){
        log.info("Request to return supported custom commands for simulation {} received.", type);
        return ResponseEntity.ok(service.getSupportedCustomCommandsOfSimulation(type));
    }

    @Operation(summary = "Get example for a supported custom command.",
            description = "Returns an example representing the expected input for a given custom command of a simulation type." +
                    "To get the supported custom commands of a simulation type, use the endpoint /type/{type}/custom-commands.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Example fetched",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @Schema(implementation = ExecutionCommand.CustomCommand.class),
                            examples = @ExampleObject(value = "{\"type\":\"CUSTOM\", \"jsonCommand\": \"{\"customCommandType\":\"TURN_AROUND\", \"direction\":\"LEFT\"}\"}")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping(value="/type/{type}/custom-commands/{commandType}/example")
    public ResponseEntity<ExecutionCommand.CustomCommand> getSupportedCustomCommandExample(@PathVariable String type, @PathVariable String commandType){
        log.info("Request to return custom command example for simulation {} for command type {} received.", type, commandType);
        return ResponseEntity.ok(new ExecutionCommand.CustomCommand(service.getCustomCommandExampleJson(type, commandType)));
    }

    @Operation(summary = "Get Simulation Instances",
            description = "Returns all registered simulation instances for all available simulation types with the corresponding simulation type, the host and port." +
                    "If an instance is included in the response, it might still not be available if the instance is associated with an active session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Simulation instances fetched",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            array = @ArraySchema(schema = @Schema(implementation = SimulationInstanceConfig.class)),
                            examples = @ExampleObject(value = "[{\"simulationType\":\"WEBOTS\", \"instanceId\":\"MY_PERSONAL_WEBOTS_INSTANCE\",\"instanceHost\":\"localhost\", \"instancePort\":10010}]")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping(value = "/instance", produces = "application/json")
    public ResponseEntity<List<SimulationInstanceConfig>> getSimulationInstances(){
        log.info("Request to return available simulation instances received.");
        return ResponseEntity.ok(service.getSimulationInstances());
    }

    @Operation(summary = "Get Simulation Instances for a specific Simulation Type",
            description = "Returns all registered simulation instances for a given simulation type." +
                    "If an instance is included in the response, it might still not be available if the instance is associated with an active session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Simulation instances fetched",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            array = @ArraySchema(schema = @Schema(implementation = SimulationInstanceConfig.class)),
                            examples = @ExampleObject(value = "[{\"simulationType\":\"WEBOTS\", \"instanceId\":\"MY_PERSONAL_WEBOTS_INSTANCE\",\"instanceHost\":\"localhost\", \"instancePort\":10010}]")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @GetMapping("/{type}/instance")
    public ResponseEntity<List<SimulationInstanceConfig>> getSimulationInstances(@Parameter(description = "The type of the simulation, e.g. WEBOTS", required = true)
                                                                                     @PathVariable String type){
        log.info("Request to return available simulation instances of simulation {} received.", type);
        return ResponseEntity.ok(service.getSimulationInstances(type));
    }
    @Operation(summary = "Register simulation instance",
            description = "Registers a simulation instance for the indicated type. For a given type, no two instances can have the same id" +
                    "and no two instances can have the same combination of host and port.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Registration successful"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @PostMapping(value = "/instance", consumes = "application/json")
    public ResponseEntity<Void> registerSimulationInstanceForAdaptor(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The configuration of the instance.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SimulationInstanceConfig.class),
                    examples = @ExampleObject(value = "{\"simulationType\":\"WEBOTS\", \"instanceId\":\"MY_PERSONAL_WEBOTS_INSTANCE\",\"instanceHost\":\"localhost\", \"instancePort\":10010}")))
                                                                         @RequestBody SimulationInstanceConfig config) throws Exception {
        log.info("Request to register simulation instance {} for simulation {} received.", config.getInstanceId(), config.getSimulationType());
        service.registerSimulationInstanceForAdaptor(config);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Delete simulation instance",
            description = "Deletes the simulation instance of the given type.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Deletion successful"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    @DeleteMapping("/{type}/instance/{instanceId}")
    public ResponseEntity<Void> deleteSimulationInstance(@Parameter(description = "The type of the simulation, e.g. WEBOTS", required = true)
                                                             @PathVariable String type,
                                                         @Parameter(description = "The id of the simulation instance to be deleted.")
                                                         @PathVariable String instanceId){
        log.info("Request to delete simulation instance {} of simulation {} received.", instanceId, type);
        service.deleteSimulationInstance(type, instanceId);
        return ResponseEntity.ok().build();
    }
}
