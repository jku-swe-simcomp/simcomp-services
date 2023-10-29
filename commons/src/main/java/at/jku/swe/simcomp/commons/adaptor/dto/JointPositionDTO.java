package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JointPositionDTO {
    RoboJoint joint;
    Double degree;
}
