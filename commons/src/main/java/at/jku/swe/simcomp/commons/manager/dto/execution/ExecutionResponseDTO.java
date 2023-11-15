package at.jku.swe.simcomp.commons.manager.dto.execution;

public record ExecutionResponseDTO(String simulationName, Long responseCode, ExecutionResponseState state, String report) {
}
