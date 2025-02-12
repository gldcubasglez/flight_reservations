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
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class SeatsUseCaseImplIntegrationTests {
    @Autowired
    private FlightReservationService flightReservationService;
    @Autowired
    private SeatRepository seatRepository;


    @Test
    void testSeats() {
        try {
            List<SeatEntity> seatEntityList = seatRepository.findAllAvailable();
            if (!seatEntityList.isEmpty()) {
                SeatEntity seatEntity = seatEntityList.getFirst();
                Flight seatList = flightReservationService.checkAvailableFlightSeats(seatEntity.getFlight().getId());
                Assertions.assertFalse(seatList.getSeats().isEmpty());
            }
            else
                assert false;
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void testFlightNotExists() {
        BadRequestException thrown = Assertions.assertThrows(BadRequestException.class, () -> {
            flightReservationService.checkAvailableFlightSeats(434324L);
        });
        Assertions.assertEquals("El vuelo no existe.", thrown.getMessage());
    }


//    @Test
//    void testNoSeats() {
//        try {
//            List<SeatEntity> seatEntityList = seatRepository.findAllAvailable();
//            Optional<SeatEntity> seatEntityOptional = seatEntityList.isEmpty() ? Optional.empty() : Optional.of(seatEntityList.getFirst());
//            if (seatEntityOptional.isPresent()) {
//                SeatEntity seatEntity = seatEntityOptional.get();
//                List<Seat> seatList = flightReservationService.checkAvailableFlightSeats(seatEntity.getFlightId());
//                Assertions.assertTrue(seatList.isEmpty());
//            }
//            else assert false;
//        } catch (BadRequestException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
