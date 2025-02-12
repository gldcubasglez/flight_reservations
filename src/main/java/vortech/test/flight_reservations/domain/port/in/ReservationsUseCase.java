package vortech.test.flight_reservations.domain.port.in;

import org.apache.coyote.BadRequestException;
import vortech.test.flight_reservations.domain.model.Reservation;

import java.util.List;

public interface ReservationsUseCase {

    List<Reservation> getConfirmedReservations(Long flightId) throws BadRequestException;
}
