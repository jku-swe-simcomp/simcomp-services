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

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionKey(UUID sessionKey);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.state = :state WHERE s.sessionKey = :sessionKey")
    void updateSessionStateBySessionKey(UUID sessionKey, SessionState state);


    default Session findBySessionKeyOrElseThrow(UUID sessionKey) throws NotFoundException{
        return findBySessionKey(sessionKey).orElseThrow(() ->new NotFoundException("Session with key %s not found.".formatted(sessionKey)));
    }
}
