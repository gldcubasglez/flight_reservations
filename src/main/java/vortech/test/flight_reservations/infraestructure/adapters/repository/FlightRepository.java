package vortech.test.flight_reservations.infraestructure.adapters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;


@Repository
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
    @Query("select u from FlightEntity u where u.id = :flightId")
    FlightEntity findByFlightId(@Param("flightId") Long flightId);
}


