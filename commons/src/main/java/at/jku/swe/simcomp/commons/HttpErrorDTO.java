package at.jku.swe.simcomp.commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import at.jku.swe.simcomp.commons.adaptor.endpoint.ExecutionErrorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private long status;
    private String message;
}
