package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoboJointStateDTO {
    @NonNull
    private RoboJoint joint;
    @NonNull
    private double position;
    @NonNull
    private double velocity;
    @NonNull
    private double effort;
}
