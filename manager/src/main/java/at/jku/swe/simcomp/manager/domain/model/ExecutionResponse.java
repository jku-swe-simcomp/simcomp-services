package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "execution_response")
@AllArgsConstructor
@NoArgsConstructor
@ToString
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
    @ToString.Exclude
    private AdaptorSession adaptorSession;

    @Column(name = "response_code")
    private Long responseCode;

    @Column
    private ExecutionResponseState state;

    @Column
    private String report;
}