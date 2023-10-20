package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionCommandDTO {
    @NonNull
    private ExecutionCommandType executionCommandType;
    @NonNull
    private RoboStateDTO endState;
}
