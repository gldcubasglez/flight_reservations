package vortech.test.flight_reservations.application.usecases;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import vortech.test.flight_reservations.domain.port.in.GenerateDefaultDataUseCase;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.ReservationRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.*;

@Transactional
@Service
public class GenerateDefaultDataUseCaseImpl implements GenerateDefaultDataUseCase {
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;

    public GenerateDefaultDataUseCaseImpl(SeatRepository seatRepository, FlightRepository flightRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
    }


    @Transactional
    @Override
    public Map<String, Object> generateDefaultData() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<FlightEntity> flights = createTestFlights();
            List<SeatEntity> seats = createTestSeats(flights);
            createTestReservation();

//            result.put("flights", flights);
            result.put("seats", seats);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<FlightEntity> createTestFlights() {
        List<FlightEntity> flights = new ArrayList<>();

        flights.add(new FlightEntity("Madrid", "Barcelona", new Date(), new Date(System.currentTimeMillis() + 3600000)));
        flights.add(new FlightEntity("Valencia", "Salamanca", new Date(), new Date(System.currentTimeMillis() + 7200000)));
        flights.add(new FlightEntity("Zaragoza", "Murcia", new Date(), new Date(System.currentTimeMillis() + 5400000)));

        flightRepository.saveAll(flights);
        return flights;
    }

    private List<SeatEntity> createTestSeats(List<FlightEntity> flights) {
        List<SeatEntity> seats = new ArrayList<>();

        for (FlightEntity flight : flights) {
            String[] seatNumbers = {"1A", "1B", "2A", "2B"};

            for (String seatNumber : seatNumbers) {
                SeatEntity seat = new SeatEntity(seatNumber, flight);
                flight.addSeat(seat);
                seats.add(seat);
            }
        }

        seatRepository.saveAll(seats);
        return seats;
    }

//    esta funcion se ejecuta para crear todas las reservaciones para un vuelo
//    en el test debe dar el error de asientos no disponibles
    private void createTestReservation() {
        List<ReservationEntity> reservations = new ArrayList<>();

        FlightEntity flight = new FlightEntity("Madrid", "Paris", new Date(), new Date(System.currentTimeMillis() + 3600000));
        flight = flightRepository.save(flight);

        String[] seatNumbers = {"1A", "1B", "2A", "2B"};

        List<SeatEntity> seats = new ArrayList<>();
        for (String seatNumber : seatNumbers) {
            SeatEntity seat = new SeatEntity(seatNumber, flight);
            seat.setAvailable(false);
            flight.addSeat(seat);
            seat = seatRepository.save(seat);
            seats.add(seat);
        }

        ReservationEntity reservationEntity = new ReservationEntity("Marlene", "marlene@gmail.com", seats, flight);
        reservations.add(reservationEntity);
        reservationRepository.saveAll(reservations);

        reservationEntity.getSeats()
                .forEach(seat -> {
                    seat.setReservation(reservationEntity);
                    seat.setAvailable(false);
                });


    }
}
