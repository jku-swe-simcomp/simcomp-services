package at.jku.swe.simcomp.commons.adaptor.dto;

import lombok.*;

import java.util.List;

/**
 * A rich DTO which contains data required for the execution
 * of an action defined by the {@link ActionType}.
 *
 * Not all fields might be required for a given action, e.g. {@link ActionType#STOP}
 * would require no additional data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionCommandDTO {
    @NonNull
    private ActionType actionType;
    /**
     * Required for action types...
     */
    private PositionDTO position;
    private QuaternionDTO orientation;
    List<JointAngleAdjustmentDTO> jointAdjustments;
    private double speed;
}
