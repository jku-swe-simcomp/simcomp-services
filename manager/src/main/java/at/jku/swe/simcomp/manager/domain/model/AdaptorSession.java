package at.jku.swe.simcomp.manager.domain.model;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.commons.manager.dto.session.SessionState;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a session of an adaptor.
 */
@Getter
@Setter
@Builder
@Entity
@Table(name = "adaptor_session")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdaptorSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "adaptor_name", nullable = false)
    private String adaptorName;

    @Column(name = "instance_id", nullable = true)
    private String instanceId;

    @ManyToOne
    @JoinColumn(name = "aggregated_session_id", nullable = false)
    @ToString.Exclude
    private Session session;

    @Column(name = "adaptor_session_key", nullable = false)
    private String sessionKey;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)

    @Builder.Default
    @OneToMany(mappedBy = "adaptorSession", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JointPositions> jointPositions = new ArrayList<>();

    private SessionState state;

    public void addJointPositions(JointPositions j){
        j.setAdaptorSession(this);
        jointPositions.add(j);
    }

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