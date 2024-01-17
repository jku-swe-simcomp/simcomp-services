package at.jku.swe.simcomp.commons.manager.dto.execution;

/**
 * This enum is used to represent the state of an execution.
 */
public enum ExecutionResponseState {
    SUCCESS,
    ERROR,
    TIMEOUT,
    RUNNING,
    WAITING
}
