package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.model.SessionState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionKey(String sessionKey);
    void deleteBySessionKey(String sessionKey);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.state = :state WHERE s.sessionKey = :sessionKey")
    void updateSessionStateBySessionKey(String sessionKey, SessionState state);
}
