package vortech.test.flight_reservations.integration;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vortech.test.flight_reservations.application.services.FlightReservationService;
import vortech.test.flight_reservations.domain.model.Flight;
import vortech.test.flight_reservations.domain.model.Reservation;
import vortech.test.flight_reservations.domain.model.Seat;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.*;

@SpringBootTest
@Transactional
class MakeReservationsUseCaseImplIntegrationTests {
    @Autowired
    private FlightReservationService flightReservationService;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private SeatRepository seatRepository;


    // para que esta prueba funciona hay q tomar un vuelo que tenga asientos disponibles y no disp
    @Test
    void testSeatNotExists() {
        List<FlightEntity> flightList = flightRepository.findAll();
        if (!flightList.isEmpty()) {
            FlightEntity flight = flightList.getFirst();
            Reservation reservation = getReservationFromFlightNoSeat(flight);
            BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
                flightReservationService.makeReservation(reservation);
            });
            Assertions.assertEquals("El asiento no existe.", thrown.getMessage());
        } else assert false;
    }

    //    para que funcione este test, tengo que crear una reservacion con 1 solo asiento para usarlo aca
//    @Test
    void testSeatNotAvailable() {
        List<FlightEntity> flightList = flightRepository.findAll();
        if (!flightList.isEmpty()) {
            for (FlightEntity obj : flightList) {
                List<SeatEntity> seatEntityList = seatRepository.findUnavailableByFlightId(obj);

                if (!seatEntityList.isEmpty()) {
                    BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
                        Reservation reservation = getReservationFromSeats(seatEntityList);
                        flightReservationService.makeReservation(reservation);
                    });
                    Assertions.assertEquals("El asiento no está disponible.", thrown.getMessage());
                }
            }
        } else assert false;
    }

    @Test
    void testFlightNotAvailable() {
        List<SeatEntity> seatEntityList = seatRepository.findAllUnavailable();
        if (!seatEntityList.isEmpty()) {
            Reservation reservation = getReservationFromSeats(seatEntityList);
            BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
                flightReservationService.makeReservation(reservation);
            });
            Assertions.assertEquals("El vuelo no tiene asientos disponibles.", thrown.getMessage());
        } else assert false;
    }

    @Test
    void testFlightNotExists() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation("name", "email@gmail.com", new ArrayList<>(), new Flight()));
        });
        Assertions.assertEquals("El vuelo no existe.", thrown.getMessage());
    }

    @Test
    void testReservationOk() {
        try {
            List<SeatEntity> seatList = seatRepository.findAll();
            if (!seatList.isEmpty()) {
                Reservation reservation = getReservationFromSeats(seatList);
                String result = flightReservationService.makeReservation(reservation);
                Assertions.assertEquals("Su solicitud se está procesando.", result);
            }
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }

    private static Reservation getReservationFromSeats(List<SeatEntity> seatList) {
        SeatEntity seatEntity = seatList.getFirst();

        Flight flight = new Flight();
        flight.setId(seatEntity.getFlight().getId());
        flight.setOrigin(seatEntity.getFlight().getOrigin());
        flight.setDestination(seatEntity.getFlight().getDestination());
        flight.setArrivalTime(seatEntity.getFlight().getArrivalTime());
        flight.setDepartureTime(seatEntity.getFlight().getDepartureTime());

        Seat seat = new Seat();
        seat.setId(seatEntity.getId());
        seat.setFlight(flight);
        seat.setAvailable(seatEntity.isAvailable());
        seat.setSeatNumber(seatEntity.getSeatNumber());

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setSeats(Collections.singletonList(seat));
        reservation.setName("name");
        reservation.setEmail("email@gmail.com");

        seat.setReservation(reservation);
        flight.setSeats(Collections.singletonList(seat));
        return reservation;
    }

    private static Reservation getReservationFromFlightNoSeat(FlightEntity flightEntity) {
        Flight flight = new Flight();
        flight.setId(flightEntity.getId());
        flight.setOrigin(flightEntity.getOrigin());
        flight.setDestination(flightEntity.getDestination());
        flight.setArrivalTime(flightEntity.getArrivalTime());
        flight.setDepartureTime(flightEntity.getDepartureTime());

        List<Seat> seats = new ArrayList<>();

        Seat seat = new Seat();
        seat.setId(432423L);
        seat.setFlight(flight);
        seat.setAvailable(true);
        seat.setSeatNumber("45");
        seats.add(seat);


        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setSeats(seats);
        reservation.setName("name");
        reservation.setEmail("email@gmail.com");
        return reservation;
    }
}
