package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

/**
 * Represents the result of an execution.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionResultDTO {
    private String report;
}
