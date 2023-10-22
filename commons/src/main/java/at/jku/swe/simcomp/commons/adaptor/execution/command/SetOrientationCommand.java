package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.QuaternionDTO;
import lombok.NonNull;

public record SetOrientationCommand(@NonNull QuaternionDTO orientation) implements ExecutionCommand {
}
