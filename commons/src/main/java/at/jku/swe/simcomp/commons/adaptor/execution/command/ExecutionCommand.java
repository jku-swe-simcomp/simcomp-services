package at.jku.swe.simcomp.commons.adaptor.execution.command;

import at.jku.swe.simcomp.commons.adaptor.dto.JointAngleAdjustmentDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.JointPositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.PositionDTO;
import at.jku.swe.simcomp.commons.adaptor.dto.QuaternionDTO;
import lombok.NonNull;

import java.util.List;

public interface ExecutionCommand {
    public static record PoseCommand(@NonNull PositionDTO position, @NonNull QuaternionDTO orientation) implements ExecutionCommand {}
    public static record SetPositionCommand(@NonNull PositionDTO position) implements ExecutionCommand {}
    public static record SetOrientationCommand(@NonNull QuaternionDTO orientation) implements ExecutionCommand {}
    public static record AdjustJointAnglesCommand(@NonNull List<JointAngleAdjustmentDTO> jointAngleAdjustment) implements ExecutionCommand {}
    public static record SetJointPositionsCommand(@NonNull List<JointPositionDTO> jointPosition) implements ExecutionCommand {}
    public static record SetSpeedCommand(@NonNull Double speed) implements ExecutionCommand {}
    public static record PauseCommand() implements ExecutionCommand {}
    public static record ResumeCommand() implements ExecutionCommand {}
    public static record ResetToHomeCommand() implements ExecutionCommand {}
    public static record StopCommand() implements ExecutionCommand {}
    public static record CalibrateCommand() implements ExecutionCommand {}
    public static record ToggleGripperModeCommand() implements ExecutionCommand {}
    public static record GrabCommand() implements ExecutionCommand {}
    public static record OpenHandCommand() implements ExecutionCommand {}

}
