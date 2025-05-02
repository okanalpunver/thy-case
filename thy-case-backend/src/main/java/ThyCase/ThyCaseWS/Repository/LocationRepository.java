package ThyCase.ThyCaseWS.Repository;

import ThyCase.ThyCaseWS.Entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    boolean existsByLocationCode(String code);
}
