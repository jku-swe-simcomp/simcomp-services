package at.jku.swe.simcomp.commons.manager.dto.execution;

/**
 * This DTO is used to represent the response of an execution.
 *
 * @param simulationName The name of the simulation
 * @param responseCode The response code
 * @param state The state of the execution
 * @param report The report of the execution
 */
public record ExecutionResponseDTO(String simulationName, Long responseCode, ExecutionResponseState state, String report) {
}
