package at.jku.swe.simcomp.commons.manager.dto.execution;
import java.time.LocalDateTime;
import java.util.List;
/**
 * This DTO is used to represent an execution of a command.
 */
public record ExecutionDTO(String id, String sessionId, String command, LocalDateTime createdAt, List<ExecutionResponseDTO> responses) {
}
