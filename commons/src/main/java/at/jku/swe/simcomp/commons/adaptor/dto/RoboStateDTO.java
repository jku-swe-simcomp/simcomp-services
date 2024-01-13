package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

import java.util.List;

/**
 * Represents the state of the robot.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoboStateDTO {
    @NonNull
    private PoseDTO pose;
    @NonNull
    private List<RoboJointStateDTO> jointStates;
}
