package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the position of a joint
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JointPositionDTO {
    RoboJoint joint;
    Double radians;
}
