package vortech.test.flight_reservations.infraestructure.adapters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.List;


@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    @Query("select u from SeatEntity u where u.flight = :flight and u.available = true")
    List<SeatEntity> findAllByFlightId(@Param("flight") FlightEntity flight);

    @Query("select u from SeatEntity u where u.id = :seatId")
    SeatEntity findBySeatId(@Param("seatId") Long seatId);

    @Query("select u from SeatEntity u where u.id = :seatId and u.flight = :flight")
    SeatEntity findBySeatIdAAndFlightId(@Param("seatId") Long seatId, @Param("flight") FlightEntity flight);

    @Query("select u from SeatEntity u where u.available = true")
    List<SeatEntity> findAllAvailable();

    @Query("select u from SeatEntity u where u.available = false")
    List<SeatEntity> findAllUnavailable();

    @Query("select u from SeatEntity u where u.flight = :flight and u.available = false")
    List<SeatEntity> findUnavailableByFlightId(@Param("flight") FlightEntity flight);
}


