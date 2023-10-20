package at.jku.swe.simcomp.serviceregistry.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adaptor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adaptor {
    @Id
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "host", nullable = false)
    private String host;

    @Column(name = "port", nullable = false)
    private Integer port;

    @OneToMany(mappedBy = "adaptor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdaptorEndpoint> adaptorEndpoints = new ArrayList<>();
}
