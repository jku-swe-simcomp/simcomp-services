package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import at.jku.swe.simcomp.manager.domain.model.AdaptorSession;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AdaptorSessionRepository extends JpaRepository<AdaptorSession, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE AdaptorSession s SET s.state = :state WHERE s.id = :id")
    void updateSessionStateById(Long id, SessionState state);

    List<AdaptorSession> findBySessionSessionKey(UUID sessionKey);

    List<AdaptorSession> findByState(SessionState state);
}
