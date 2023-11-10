package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "execution")
public class Execution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "execution_id", nullable = false, unique = true)
    private UUID executionId;

    @ManyToOne
    @JoinColumn(name="session_id", nullable=false)
    private Session session;

    @Column(name = "command", nullable = false)
    private String command;

    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExecutionResponse> responses;

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