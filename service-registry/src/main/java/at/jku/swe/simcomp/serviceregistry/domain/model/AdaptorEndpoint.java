package at.jku.swe.simcomp.serviceregistry.domain.model;

import at.jku.swe.simcomp.commons.adaptor.endpoint.dto.AdaptorEndpointType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adaptor_endpoint")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdaptorEndpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "endpoint_type", nullable = false)
    private AdaptorEndpointType endpointType;

    @Column(name = "path", nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adaptor_name")
    private Adaptor adaptor;
}
