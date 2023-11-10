package at.jku.swe.simcomp.manager.domain.repository;

import at.jku.swe.simcomp.manager.domain.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
