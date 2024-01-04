package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

/**
 * Repository for {@link AdaptorSession}
 */
public interface AdaptorSessionRepository extends JpaRepository<AdaptorSession, Long> {
    /**
     * Updates the state of the adaptor session.
     * @param id the id (internal) of the session
     * @param state the new state
     */
    @Transactional
    @Modifying
    @Query("UPDATE AdaptorSession s SET s.state = :state WHERE s.id = :id")
    void updateSessionStateById(Long id, SessionState state);

    /**
     * Returns  a list of {@link AdaptorSession} for a given session key.
     * @param sessionKey the session key
     * @return a list of {@link AdaptorSession}
     */
    List<AdaptorSession> findBySessionSessionKey(UUID sessionKey);

    /**
     * Returns a list of {@link AdaptorSession} for a given state.
     * @param state the state
     * @return a list of {@link AdaptorSession}
     */
    List<AdaptorSession> findByState(SessionState state);
}
