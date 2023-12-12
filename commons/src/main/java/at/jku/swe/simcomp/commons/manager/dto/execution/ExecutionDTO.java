package at.jku.swe.simcomp.commons.manager.dto.execution;
import java.time.LocalDateTime;
import java.util.List;

public record ExecutionDTO(String id, String sessionId, String command, LocalDateTime createdAt, List<ExecutionResponseDTO> responses) {
}
