package ThyCase.ThyCaseWS.Repository;

import ThyCase.ThyCaseWS.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByIso2(String iso2);
}
