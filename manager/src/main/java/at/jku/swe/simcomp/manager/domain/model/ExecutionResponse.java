package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "execution_response")
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ExecutionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="execution_id", nullable=false, updatable = false)
    @ToString.Exclude
    private Execution execution;

    @ManyToOne
    @JoinColumn(name="adaptor_session_id", nullable=false, updatable = false)
    @ToString.Exclude
    private AdaptorSession adaptorSession;

    @Column(name = "response_code")
    private Long responseCode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionResponseState state;

    @Column
    private String report;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}