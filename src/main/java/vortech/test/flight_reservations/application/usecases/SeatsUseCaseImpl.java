package vortech.test.flight_reservations.application.usecases;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import vortech.test.flight_reservations.domain.model.Flight;
import vortech.test.flight_reservations.domain.model.Seat;
import vortech.test.flight_reservations.domain.port.in.SeatsUseCase;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatsUseCaseImpl implements SeatsUseCase {
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;

    public SeatsUseCaseImpl(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

    // no se implementan funciones de CRUD pq no eran objetivo de la prueba

    @Override
    public Flight checkAvailableFlightSeats(Long flightId) throws BadRequestException {
        FlightEntity flightEntity = flightRepository.findByFlightId(flightId);
        validateData(flightEntity);

        List<SeatEntity> allSeats = seatRepository.findAllByFlightId(flightEntity);

        List<Seat> availableSeats = allSeats.stream()
                .filter(seatEntity -> seatEntity.isAvailable())
                .map(seatEntity -> {
                    Seat seat = new Seat();
                    seat.setId(seatEntity.getId());
                    seat.setSeatNumber(seatEntity.getSeatNumber());
                    return seat;
                })
                .collect(Collectors.toList());

        Flight flight = new Flight();
        flight.setId(flightEntity.getId());
        flight.setOrigin(flightEntity.getOrigin());
        flight.setDestination(flightEntity.getDestination());
        flight.setArrivalTime(flightEntity.getArrivalTime());
        flight.setDepartureTime(flightEntity.getDepartureTime());
        flight.setSeats(availableSeats);

        return flight;
    }

    private void validateData(FlightEntity flightEntity) throws BadRequestException {
        if (flightEntity == null)
            throw new BadRequestException("El vuelo no existe.");
    }
}
