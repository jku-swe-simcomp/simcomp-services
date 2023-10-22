package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import lombok.NonNull;

import java.util.List;

public record AdjustJointAngleCommand(@NonNull List<JointAngleAdjustmentDTO> jointAdjustments) implements ExecutionCommand{
}
