package at.jku.swe.simcomp.manager.domain.model;

import at.jku.swe.simcomp.commons.manager.dto.execution.ExecutionResponseState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class represents a response from an adaptor for an execution.
 */
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

    @Column(length = 2550)
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