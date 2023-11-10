package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@Table(name = "adaptor_session")
public class AdaptorSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "adaptor_name", nullable = false)
    private String adaptorName;

    @ManyToOne
    @JoinColumn(name = "aggregated_session_id", nullable = false)
    private Session session;

    @Column(name = "adaptor_session_key", nullable = false)
    private String sessionKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}