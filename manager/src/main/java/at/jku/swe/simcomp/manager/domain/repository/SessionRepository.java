package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.manager.domain.model.Session;
import at.jku.swe.simcomp.manager.domain.model.SessionState;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionKey(UUID sessionKey);
    void deleteBySessionKey(UUID sessionKey);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.state = :state WHERE s.sessionKey = :sessionKey")
    void updateSessionStateBySessionKey(UUID sessionKey, SessionState state);
}
