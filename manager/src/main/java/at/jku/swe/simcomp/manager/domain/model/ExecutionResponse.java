package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "execution_response")
public class ExecutionResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="execution_id", nullable=false)
    private Execution execution;

    @ManyToOne
    @JoinColumn(name="adaptor_session_id", nullable=false)
    private AdaptorSession adaptorSession;

    @Column(name = "response_code")
    private Long responseCode;

    @Column
    private ExecutionResponseState state;

    @Column
    private String report;
}