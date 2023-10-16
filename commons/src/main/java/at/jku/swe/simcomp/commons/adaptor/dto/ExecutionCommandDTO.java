package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionCommandDTO {
    @NonNull
    private ExecutionCommandType executionCommandType;
    @NonNull
    private PoseDTO pose;
    @NonNull
    private List<RoboJointStateDTO> targetJoints;
}
