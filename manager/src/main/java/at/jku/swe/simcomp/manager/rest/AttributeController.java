package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.HttpErrorDTO;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.manager.service.AttributeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for managing attributes.
 * Refer to the OpenAPI documentation for more information.
 */
@RestController
@RequestMapping("/session/{sessionId}/attribute/{attributeKey}")
@Slf4j
@Tag(name = "Attribute Controller", description = "Endpoints for managing attributes")
public class AttributeController {
   private final AttributeService attributeService;
   public AttributeController(AttributeService attributeService){
       this.attributeService = attributeService;
    }

    @GetMapping
    @Operation(summary = "Get Attributes", description = "Retrieve attribute values from all simulation instances associated with this session. " +
            "The attribute values returned by the simulation instances are merged into a single map." +
            "The type of the simulation instance is used as key for the map, the fetched attribute value is used as value for the map."+
            "The matching of the requested attribute key to the correct attribute-value-type is not enforced by the manager; i.e. if the simulation instance does not provide an attribute value that matches the requested attribute, it is still included."+
            "If any error occurs while fetching an attribute value from a simulation instance, or no attribute value is returned by a simulation instance, the value is set to null.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Attributes retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AttributeValue.class),
                            examples = @ExampleObject(value = "{ \"key\": \"JOINT_POSITIONS\", \"jointPositions\": [0.0,0.0,0.0,0.0,0.0,0.0]}")
                    )),
            @ApiResponse(responseCode = "404",
                    description = "No session with given id found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples = @ExampleObject(value = "{\"type\":\"ERROR\",\"status\":404, \"message\":\"No session with given id found.\"}")
                    )),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HttpErrorDTO.class),
                            examples =@ExampleObject(value = "{\"type\":\"ERROR\",\"status\":500, \"message\":\"Internal server error.\"}")
                    ))
    })
    public ResponseEntity<Map<String, AttributeValue>> getAttributes(
            @Parameter(description = "ID of the session. Sessions can be initialized with the Session Controller", in = ParameterIn.PATH, required = true) @PathVariable UUID sessionId,
            @Parameter(description = "The key of the requested attribute.",
                    in = ParameterIn.PATH, required = true) @PathVariable AttributeKey attributeKey){
        log.info("Request to fetch attribute {} for session {}", attributeKey, sessionId);
        return ResponseEntity.ok(attributeService.getAttributeValues(sessionId, attributeKey));
    }
}
