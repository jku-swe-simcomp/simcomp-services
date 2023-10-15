package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoboJointStateDTO {
    private RoboJoint joint;
    private double position;
    private double velocity;
    private double effort;
}
