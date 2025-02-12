package vortech.test.flight_reservations.application.usecases;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import vortech.test.flight_reservations.domain.model.Flight;
import vortech.test.flight_reservations.domain.model.Reservation;
import vortech.test.flight_reservations.domain.model.Seat;
import vortech.test.flight_reservations.domain.port.in.ReservationsUseCase;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.ReservationRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationsUseCaseImpl implements ReservationsUseCase {
    private final ReservationRepository reservationRepository;
    private final FlightRepository flightRepository;

    public ReservationsUseCaseImpl(ReservationRepository reservationRepository, FlightRepository flightRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Reservation> getConfirmedReservations(Long flightId) throws BadRequestException {
        FlightEntity flightEntity = flightRepository.findByFlightId(flightId);
        validateData(flightEntity);

        List<ReservationEntity> confirmedReservations = reservationRepository.findAllByFlightIdConfirmed(flightEntity);

        List<Reservation> reservationList = confirmedReservations.stream()
                .map(entity -> {
                    Reservation reservation = new Reservation();
                    reservation.setId(entity.getId());
                    reservation.setName(entity.getName());
                    reservation.setEmail(entity.getEmail());
                    reservation.setReservationDate(entity.getReservationDate());
                    reservation.setStatus(entity.getStatus());

                    Flight flight = new Flight();
                    flight.setId(entity.getId());
                    flight.setOrigin(flightEntity.getOrigin());
                    flight.setDestination(flightEntity.getDestination());
                    flight.setArrivalTime(flightEntity.getArrivalTime());
                    flight.setDepartureTime(flightEntity.getDepartureTime());
                    reservation.setFlight(flight);

                    List<Seat> seats = new ArrayList<>();
                    for (SeatEntity seatEntity : entity.getSeats()) {
                        Long seatId = seatEntity.getId();
                        Seat seat = new Seat();
                        seat.setId(seatId);
                        seat.setAvailable(seatEntity.isAvailable());
                        seat.setSeatNumber(seatEntity.getSeatNumber());
                        seats.add(seat);
                    }
                    reservation.setSeats(seats);

                    return reservation;
                })
                .collect(Collectors.toList());

        return reservationList;
    }

    private void validateData(FlightEntity flightEntity) throws BadRequestException {
        if (flightEntity == null)
            throw new BadRequestException("El vuelo no existe.");
    }
}
