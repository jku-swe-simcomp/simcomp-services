package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionResponseDTO {
    @NonNull
    private boolean success;
    private String message;
    @NonNull
    private RoboStateDTO currentState;
}
