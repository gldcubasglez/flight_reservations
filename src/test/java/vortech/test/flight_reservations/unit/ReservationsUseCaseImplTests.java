package vortech.test.flight_reservations.unit;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vortech.test.flight_reservations.application.services.FlightReservationService;

@SpringBootTest
@Transactional
class ReservationsUseCaseImplTests {
    @Autowired
    private FlightReservationService flightReservationService;


}
