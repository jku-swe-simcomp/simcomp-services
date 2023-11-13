package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.manager.domain.model.Execution;
import at.jku.swe.simcomp.manager.domain.model.ExecutionResponseState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.UUID;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    Optional<Execution> findByExecutionId(UUID executionId);

    default Execution findByExecutionUUIDOrElseThrow(UUID executionId) throws NotFoundException {
        return findByExecutionId(executionId).orElseThrow(() ->new NotFoundException("Execution with UUID %s not found.".formatted(executionId)));
    }
}
