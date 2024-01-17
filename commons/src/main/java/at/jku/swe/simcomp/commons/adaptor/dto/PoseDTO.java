package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

import java.util.List;

/**
 * Represents the pose of the end-effector.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoseDTO {
    @NonNull
    private PositionDTO position;
    @NonNull
    private OrientationDTO orientation;
}
