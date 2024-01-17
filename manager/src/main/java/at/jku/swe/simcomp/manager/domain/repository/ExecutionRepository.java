package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.manager.domain.model.Execution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link Execution}
 */
public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    /**
     * Returns a list of {@link Execution} for a given id.
     * @param executionId the id
     * @return the execution, empty otherwise
     */
    Optional<Execution> findByExecutionId(UUID executionId);

    /**
     * Returns an exeuction or throws an exception if not found.
     * @param executionId the id
     * @return the execution
     * @throws NotFoundException if not found
     */
    default Execution findByExecutionUUIDOrElseThrow(UUID executionId) throws NotFoundException {
        return findByExecutionId(executionId).orElseThrow(() ->new NotFoundException("Execution with UUID %s not found.".formatted(executionId)));
    }

    /**
     * Returns a list of {@link Execution} for a given session key.
     * @param sessionKey the session key
     * @return a list of {@link Execution}
     */
    List<Execution> findBySessionSessionKey(UUID sessionKey);
}
