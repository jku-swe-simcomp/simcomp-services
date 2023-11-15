package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseState;
import at.jku.swe.simcomp.manager.domain.model.ExecutionResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExecutionResponseRepository extends JpaRepository<ExecutionResponse, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE ExecutionResponse r SET r.state = :state, r.report = :message, r.responseCode = :responseCode WHERE r.id = :id")
    void updateExecutionResponse(Long id, Integer responseCode, ExecutionResponseState state, String message);
}
