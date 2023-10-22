package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import lombok.*;

import java.util.List;

public record SetJointPositionCommand(@NonNull List<JointPositionDTO> jointPositions) implements ExecutionCommand{
}
