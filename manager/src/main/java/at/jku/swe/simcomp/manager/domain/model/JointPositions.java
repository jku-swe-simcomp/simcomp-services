package at.jku.swe.simcomp.manager.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class represents the joint-positions of a simulation for a adaptor session.
 */
@Getter
@Setter
@Entity
@Table(name = "joint_positions")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JointPositions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="adaptor_session_id", nullable=false, updatable = false)
    @ToString.Exclude
    private AdaptorSession adaptorSession;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "axis_1")
    private Double axis1;
    @Column(name = "axis_2")
    private Double axis2;
    @Column(name = "axis_3")
    private Double axis3;
    @Column(name = "axis_4")
    private Double axis4;
    @Column(name = "axis_5")
    private Double axis5;
    @Column(name = "axis_6")
    private Double axis6;

    @Column(name = "message")
    private String message;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}