package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExecutionResponseDTO {
    private boolean success;
    private String message;
    private RoboStateDTO currentState;
}
