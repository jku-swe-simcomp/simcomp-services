package at.jku.swe.simcomp.commons.adaptor.execution.command;

import lombok.NonNull;

public record SetSpeedCommand(@NonNull Double speed) implements ExecutionCommand{
}
