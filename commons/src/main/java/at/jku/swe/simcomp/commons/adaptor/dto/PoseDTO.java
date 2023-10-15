package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PoseDTO {
    private PositionDTO position;
    private QuaternionDTO quaternion;
}
