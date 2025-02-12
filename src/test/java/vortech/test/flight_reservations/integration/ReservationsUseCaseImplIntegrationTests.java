package vortech.test.flight_reservations.integration;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vortech.test.flight_reservations.application.services.FlightReservationService;
import vortech.test.flight_reservations.domain.model.Reservation;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.ReservationRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class ReservationsUseCaseImplIntegrationTests {
    @Autowired
    private FlightReservationService flightReservationService;
    @Autowired
    private ReservationRepository reservationRepository;


    @Test
    void testSeats() {
        try {
            List<ReservationEntity> reservationEntityList = reservationRepository.findAll();
            if (!reservationEntityList.isEmpty()) {
                ReservationEntity reservationEntity = reservationEntityList.getFirst();
                List<Reservation> reservationList = flightReservationService.getConfirmedReservations(reservationEntity.getFlight().getId());
                Assertions.assertFalse(reservationList.isEmpty());
            }
            else
                assert false;
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testFlightNotExists() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.getConfirmedReservations(434324L);
        });
        Assertions.assertEquals("El vuelo no existe.", thrown.getMessage());
    }
}
