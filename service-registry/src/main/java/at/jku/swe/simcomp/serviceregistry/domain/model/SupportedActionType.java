package at.jku.swe.simcomp.serviceregistry.domain.model;


import at.jku.swe.simcomp.commons.adaptor.dto.ActionType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "supported_action")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportedActionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ActionType actionType;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adaptor_name")
    private Adaptor adaptor;
}
