package ThyCase.ThyCaseWS.Repository;

import ThyCase.ThyCaseWS.Entity.City;
import ThyCase.ThyCaseWS.Entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndCountry(String name, Country country);
    List<City> findByCountry(Country country);

}

