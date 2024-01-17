package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseState;
import at.jku.swe.simcomp.manager.domain.model.ExecutionResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for {@link ExecutionResponse}
 */
public interface ExecutionResponseRepository extends JpaRepository<ExecutionResponse, Long> {
    /**
     * Updates an execution response by its internal id
     * @param id the id (internal) of the execution response
     * @param state the new state
     * @param message the message
     * @param responseCode the response code
     */
    @Transactional
    @Modifying
    @Query("UPDATE ExecutionResponse r SET r.state = :state, r.report = :message, r.responseCode = :responseCode WHERE r.id = :id")
    void updateExecutionResponse(Long id, Integer responseCode, ExecutionResponseState state, String message);
}
