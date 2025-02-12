package vortech.test.flight_reservations.unit;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vortech.test.flight_reservations.application.services.FlightReservationService;

@SpringBootTest
@Transactional
class CancellationUseCaseImplTests {
    @Autowired
    private FlightReservationService flightReservationService;

    @Test
    void testNoReservationId() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.cancelReservation(0L);
        });
        Assertions.assertEquals("El id de la reservación es requerido.", thrown.getMessage());
    }

    @Test
    void testReservationNotExists() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.cancelReservation(321321L);
        });
        Assertions.assertEquals("La reservación no existe.", thrown.getMessage());
    }
}
