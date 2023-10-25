package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionResultDTO {
    private String report;
    private RoboStateDTO currentState;
}
