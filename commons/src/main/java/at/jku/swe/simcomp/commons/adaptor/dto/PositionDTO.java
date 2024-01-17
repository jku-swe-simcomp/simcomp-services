package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

/**
 * Represents the position of the end-effector.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PositionDTO {
    @NonNull
    private Double x;
    @NonNull
    private Double y;
    @NonNull
    private Double z;
}
