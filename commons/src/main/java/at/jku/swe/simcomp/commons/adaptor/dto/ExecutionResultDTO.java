package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionResultDTO {
    @NonNull
    private boolean success;
    private String message;
    private RoboStateDTO currentState;
}
