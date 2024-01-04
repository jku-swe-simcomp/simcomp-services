package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.manager.domain.model.Session;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for {@link Session}
 */
public interface SessionRepository extends JpaRepository<Session, Long> {

    /**
     * Returns a session by its session key.
     * @param sessionKey the session key
     * @return the session, empty otherwise
     */
    Optional<Session> findBySessionKey(UUID sessionKey);

    /**
     * Updates the state of the session.
     * @param sessionKey the session key
     * @param state the new state
     */
    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.state = :state WHERE s.sessionKey = :sessionKey")
    void updateSessionStateBySessionKey(UUID sessionKey, SessionState state);

    /**
     * Returns a session or throws an exception if not found.
     * @param sessionKey the session key
     * @return the session
     * @throws NotFoundException if not found
     */
    default Session findBySessionKeyOrElseThrow(UUID sessionKey) throws NotFoundException{
        return findBySessionKey(sessionKey).orElseThrow(() ->new NotFoundException("Session with key %s not found.".formatted(sessionKey)));
    }
}
