package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the angle adjustment of a joint.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JointAngleAdjustmentDTO {
    RoboJoint joint;
    Double byRadians;
}
