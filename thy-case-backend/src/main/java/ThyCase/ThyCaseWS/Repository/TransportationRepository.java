package ThyCase.ThyCaseWS.Repository;

import ThyCase.ThyCaseWS.Entity.Location;
import ThyCase.ThyCaseWS.Entity.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {
    @Query("""
      select t 
      from Transportation t
      join t.operatingDays d
      where d = :day
    """)
    List<Transportation> findByOperatingDay(@Param("day") Integer day);

    Optional<Transportation> findByOriginAndDestinationAndType(Location origin, Location destination, Transportation.TransportType transportType);
}

