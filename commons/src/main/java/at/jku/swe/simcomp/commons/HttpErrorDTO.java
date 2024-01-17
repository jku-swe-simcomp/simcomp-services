package at.jku.swe.simcomp.commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import at.jku.swe.simcomp.commons.adaptor.endpoint.ExecutionErrorDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * These DTOs are used to represent an error response from the server.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpErrorDTO.class, name = "ERROR"),
        @JsonSubTypes.Type(value = ExecutionErrorDTO.class, name = "EXECUTION_ERROR"),
})
public class HttpErrorDTO {
    @Schema(description = "The error status", example = "404")
    private long status;
    @Schema(description = "The error message", example = "Unknown error.")
    private String message;
}
