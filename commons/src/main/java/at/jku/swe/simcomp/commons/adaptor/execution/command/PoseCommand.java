package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.PoseDTO;
import lombok.*;

public record PoseCommand(@NonNull PoseDTO pose) implements ExecutionCommand{
}
