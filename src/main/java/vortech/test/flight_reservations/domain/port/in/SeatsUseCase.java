package vortech.test.flight_reservations.domain.port.in;

import org.apache.coyote.BadRequestException;
import vortech.test.flight_reservations.domain.model.Flight;
import vortech.test.flight_reservations.domain.model.Seat;

import java.util.List;

public interface SeatsUseCase {
    Flight checkAvailableFlightSeats(Long flightId) throws BadRequestException;
}
