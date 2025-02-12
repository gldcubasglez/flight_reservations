package vortech.test.flight_reservations.application.usecases;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import vortech.test.flight_reservations.domain.model.Reservation;
import vortech.test.flight_reservations.domain.model.Seat;
import vortech.test.flight_reservations.domain.port.in.MakeReservationsUseCase;
import vortech.test.flight_reservations.domain.port.out.AmqpMessageProducerPort;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MakeReservationsUseCaseImpl implements MakeReservationsUseCase {
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final AmqpMessageProducerPort amqpMessageProducerPort;

    public MakeReservationsUseCaseImpl(SeatRepository seatRepository, FlightRepository flightRepository, AmqpMessageProducerPort amqpMessageProducerPort) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
        this.amqpMessageProducerPort = amqpMessageProducerPort;
    }


    @Transactional
    @Override
    public String makeReservation(Reservation reservation) throws BadRequestException {
        try {
            validateData(reservation);
            validateRepositoryData(reservation);

            List<Long> seatIds = reservation.getSeats().stream()
                    .map(o -> {
                        return o.getId();
                    })
                    .collect(Collectors.toList());

            Map<String, Object> msg_map = new HashMap<>();
            msg_map.put("flightId", reservation.getFlight().getId());
            msg_map.put("seatIds", seatIds);
            msg_map.put("name", reservation.getName());
            msg_map.put("email", reservation.getEmail());
            amqpMessageProducerPort.sendMessagesReservation(msg_map);

            return "Su solicitud se está procesando.";
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void validateData(Reservation reservation) throws BadRequestException {
        if (reservation.getFlight() == null)
            throw new BadRequestException("El vuelo es requerido.");
        if (reservation.getName() == null || reservation.getName().isEmpty())
            throw new BadRequestException("El nombre de la persona es requerido.");
        if (reservation.getEmail() == null || reservation.getEmail().isEmpty())
            throw new BadRequestException("El correo de la persona es requerido.");
        if (!EmailValidator.isValidEmail(reservation.getEmail()))
            throw new BadRequestException("El correo de la persona es inválido.");
    }

    private void validateRepositoryData(Reservation reservation) throws BadRequestException {
        FlightEntity flightEntity = flightRepository.findByFlightId(reservation.getFlight().getId());
        if (flightEntity == null)
            throw new BadRequestException("El vuelo no existe.");

        List<SeatEntity> allByIdFlight = seatRepository.findAllByFlightId(flightEntity);
        if (allByIdFlight.isEmpty())
            throw new BadRequestException("El vuelo no tiene asientos disponibles.");

        for (Seat seat: reservation.getSeats()) {
            SeatEntity seatEntity = seatRepository.findBySeatIdAAndFlightId(seat.getId(), flightEntity);
            if (seatEntity == null)
                throw new BadRequestException("El asiento no existe.");
            if (!seatEntity.isAvailable())
                throw new BadRequestException("El asiento no está disponible.");
        }
    }
}
