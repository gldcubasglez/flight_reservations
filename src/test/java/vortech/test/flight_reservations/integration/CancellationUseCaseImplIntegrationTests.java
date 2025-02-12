package vortech.test.flight_reservations.integration;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vortech.test.flight_reservations.application.services.FlightReservationService;
import vortech.test.flight_reservations.infraestructure.adapters.repository.ReservationRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class CancellationUseCaseImplIntegrationTests {
    @Autowired
    private FlightReservationService flightReservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void testCancellationOk() {
        try {
            List<ReservationEntity> reservationList = reservationRepository.findAll();
            Optional<ReservationEntity> reservationEntityOptional = reservationList.isEmpty() ? Optional.empty() : Optional.of(reservationList.getFirst());
            if (reservationEntityOptional.isPresent()) {
                ReservationEntity reservationEntity = reservationEntityOptional.get();
                String result = flightReservationService.cancelReservation(reservationEntity.getId());
                Assertions.assertEquals("Su vuelo ha sido cancelado satifactoriamente.", result);
            }
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }
}
