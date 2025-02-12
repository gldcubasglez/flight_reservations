package vortech.test.flight_reservations.domain.port.in;

import org.apache.coyote.BadRequestException;

public interface CancellationUseCase {

    String cancelReservation(Long reservationId) throws BadRequestException;
}
