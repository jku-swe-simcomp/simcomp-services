package at.jku.swe.simcomp.serviceregistry.domain.repository;


import at.jku.swe.simcomp.serviceregistry.domain.model.Adaptor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdaptorRepository extends JpaRepository<Adaptor, String> {
}
