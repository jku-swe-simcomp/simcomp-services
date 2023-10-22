package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import lombok.NonNull;

public record SetPositionCommand(@NonNull PositionDTO position) implements ExecutionCommand{
}
