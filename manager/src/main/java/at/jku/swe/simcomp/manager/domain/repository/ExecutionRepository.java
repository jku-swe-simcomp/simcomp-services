package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.manager.domain.model.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    Optional<Execution> findByExecutionId(UUID executionId);

    default Execution findByExecutionUUIDOrElseThrow(UUID executionId) throws NotFoundException {
        return findByExecutionId(executionId).orElseThrow(() ->new NotFoundException("Execution with UUID %s not found.".formatted(executionId)));
    }

    List<Execution> findBySessionSessionKey(UUID sessionKey);
}
