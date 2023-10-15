package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RoboStateDTO {
    private ExecutionCommandType executionCommandType;
    private PoseDTO pose;
    private List<RoboJointStateDTO> targetJoints;
}
