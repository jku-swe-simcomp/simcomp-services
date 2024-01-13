package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

/**
 * Represents the state of a joint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoboJointStateDTO {
    @NonNull
    private RoboJoint joint;
    @NonNull
    private Double radians;
    @NonNull
    private Double velocity;
    @NonNull
    private Double effort;
}
