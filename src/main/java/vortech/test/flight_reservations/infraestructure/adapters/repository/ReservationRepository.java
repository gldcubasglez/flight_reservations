package vortech.test.flight_reservations.infraestructure.adapters.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;

import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("select u from ReservationEntity u where u.id = :reservationId and u.status = 'CONFIRMADA'")
    ReservationEntity findByReservationId(@Param("reservationId") Long reservationId);


    @Query("select u from ReservationEntity u where u.flight = :flight and u.status = 'CONFIRMADA'")
    List<ReservationEntity> findAllByFlightIdConfirmed(@Param("flight") FlightEntity flight);
}


