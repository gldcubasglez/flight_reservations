package vortech.test.flight_reservations.domain.port.in;

import org.apache.coyote.BadRequestException;
import vortech.test.flight_reservations.domain.model.Reservation;

public interface MakeReservationsUseCase {

    String makeReservation(Reservation reservation) throws BadRequestException;
}
