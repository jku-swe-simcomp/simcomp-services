package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class represents a command execution for a session.
 */
@Getter
@Setter
@Entity
@Table(name = "execution")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Execution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "execution_id", nullable = false, unique = true)
    private UUID executionId;

    @ManyToOne
    @JoinColumn(name="session_id", nullable=false)
    @ToString.Exclude
    private Session session;

    @Column(name = "command", length=5000)
    private String command;

    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ExecutionResponse> responses = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void addExecutionResponse(ExecutionResponse executionResponse) {
        responses.add(executionResponse);
        executionResponse.setExecution(this);
    }
}