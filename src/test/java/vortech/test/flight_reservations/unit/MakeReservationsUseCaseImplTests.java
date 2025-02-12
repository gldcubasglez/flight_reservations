package vortech.test.flight_reservations.unit;

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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class MakeReservationsUseCaseImplTests {
    @Autowired
    private FlightReservationService flightReservationService;


    @Test
    void testNullFlightId() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation("name", "email", List.of(new Seat()), null));
        });
        Assertions.assertEquals("El vuelo es requerido.", thrown.getMessage());
    }

    @Test
    void testNullName() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation(null, "email", List.of(new Seat()), new Flight()));
        });
        Assertions.assertEquals("El nombre de la persona es requerido.", thrown.getMessage());
    }

    @Test
    void testNoName() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation("", "email", List.of(new Seat()), new Flight()));
        });
        Assertions.assertEquals("El nombre de la persona es requerido.", thrown.getMessage());
    }

    @Test
    void testNullEmail() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation("name", null, List.of(new Seat()), new Flight()));
        });
        Assertions.assertEquals("El correo de la persona es requerido.", thrown.getMessage());
    }

    @Test
    void testNoEmail() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation("name", "", List.of(new Seat()), new Flight()));
        });
        Assertions.assertEquals("El correo de la persona es requerido.", thrown.getMessage());
    }

    @Test
    void testBadEmail() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.makeReservation(new Reservation("name", "email", List.of(new Seat()), new Flight()));
        });
        Assertions.assertEquals("El correo de la persona es inválido.", thrown.getMessage());
    }
}
