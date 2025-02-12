package vortech.test.flight_reservations.application.services;

import org.apache.coyote.BadRequestException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import vortech.test.flight_reservations.domain.model.Flight;
import vortech.test.flight_reservations.domain.model.Reservation;
import vortech.test.flight_reservations.domain.port.in.*;

import java.util.List;
import java.util.Map;

@Primary
@Service
public class FlightReservationService implements ReservationsUseCase, CancellationUseCase,
        MakeReservationsUseCase, SeatsUseCase, GenerateDefaultDataUseCase {

    private final ReservationsUseCase reservationUseCase;
    private final CancellationUseCase cancelReservationUseCase;
    private final MakeReservationsUseCase makeReservationsUseCase;
    private final SeatsUseCase seatsUseCase;
    private final GenerateDefaultDataUseCase generateDefaultDataUseCase;

    public FlightReservationService(ReservationsUseCase reservationUseCase, CancellationUseCase cancelReservationUseCase, MakeReservationsUseCase makeReservationsUseCase, SeatsUseCase seatsUseCase, GenerateDefaultDataUseCase generateDefaultDataUseCase) {
        this.reservationUseCase = reservationUseCase;
        this.cancelReservationUseCase = cancelReservationUseCase;
        this.makeReservationsUseCase = makeReservationsUseCase;
        this.seatsUseCase = seatsUseCase;
        this.generateDefaultDataUseCase = generateDefaultDataUseCase;
    }

    @Override
    public Flight checkAvailableFlightSeats(Long flightId) throws BadRequestException {
        return seatsUseCase.checkAvailableFlightSeats(flightId);
    }

    @Override
    public String makeReservation(Reservation reservation) throws BadRequestException {
        return makeReservationsUseCase.makeReservation(reservation);
    }

    @Override
    public String cancelReservation(Long reservationId) throws BadRequestException {
        return cancelReservationUseCase.cancelReservation(reservationId);
    }

    @Override
    public List<Reservation> getConfirmedReservations(Long flightId) throws BadRequestException {
        return reservationUseCase.getConfirmedReservations(flightId);
    }

    @Override
    public Map<String, Object> generateDefaultData() {
        return generateDefaultDataUseCase.generateDefaultData();
    }
}
