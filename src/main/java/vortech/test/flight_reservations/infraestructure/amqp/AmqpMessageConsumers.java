package vortech.test.flight_reservations.infraestructure.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vortech.test.flight_reservations.infraestructure.adapters.repository.FlightRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.ReservationRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.FlightEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.io.IOException;
import java.util.*;

@Component
public class AmqpMessageConsumers {
    private static final System.Logger logger = System.getLogger("FlightReservationsLogger");

    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final AmqpMessageProducer producer;

    public AmqpMessageConsumers(ReservationRepository reservationRepository, SeatRepository seatRepository, FlightRepository flightRepository, AmqpMessageProducer producer) {
        this.reservationRepository = reservationRepository;
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
        this.producer = producer;
    }

    @Transactional
    @RabbitListener(queues = {AmqpConfiguration.fanoutQueueReservations})
    public void receiveMessageFromFanoutQueueReservations(Message message) {
        Map<String, Object> message_map = null;
        try {
            message_map = new ObjectMapper().readValue(message.getBody(), HashMap.class);
            logger.log(System.Logger.Level.INFO, message_map.toString());

            Long flightId = Long.parseLong(message_map.getOrDefault("flightId", 0L).toString());
            List<Integer> seats = (List<Integer>) message_map.getOrDefault("seatIds", new ArrayList<>());
            String name = message_map.getOrDefault("name", "").toString();
            String email = message_map.getOrDefault("email", "").toString();

            ValidatedData result = getValidatedData(flightId, seats);
            saveData(name, email, result);

            message_map.put("success", true);
            producer.sendMessagesNotification(message_map);

        } catch (Exception e) {
            processError(e, message_map);
        }
    }

    private void saveData(String name, String email, ValidatedData result) {
        ReservationEntity reservationEntity = new ReservationEntity(name, email, result.seatEntityList(), result.flightEntity());
        reservationRepository.save(reservationEntity);

        reservationEntity.getSeats()
                .forEach(seat -> {
                    seat.setReservation(reservationEntity);
                    seat.setAvailable(false);
                });
    }

    private void processError(Exception e, Map<String, Object> message_map) {
        if (message_map != null) {
            try {
                message_map.put("success", false);
                message_map.put("error", e.getMessage());
                producer.sendMessagesNotification(message_map);
            } catch (IOException ex) {
                String error = String.format("Error notificando reservación fallida. Error: %s.", e.getMessage());
                logger.log(System.Logger.Level.ERROR, error);
            }
        }
    }

    private ValidatedData getValidatedData(Long flightId, List<Integer> seats) throws BadRequestException {
        FlightEntity flightEntity = flightRepository.findById(flightId).orElse(null);
        if (flightEntity == null)
            throw new BadRequestException("El vuelo no existe.");

        List<SeatEntity> seatEntityList = new ArrayList<>();
        SeatEntity seatEntity = null;
        for (Integer seatId : seats) {
            seatEntity = seatRepository.findBySeatIdAAndFlightId(Long.valueOf(seatId), flightEntity);
            if (seatEntity == null)
                throw new BadRequestException("El asiento no existe.");
            if (!seatEntity.isAvailable())
                throw new BadRequestException("El asiento no está disponible.");
            seatEntityList.add(seatEntity);
        }
        return new ValidatedData(flightEntity, seatEntityList);
    }

    private record ValidatedData(FlightEntity flightEntity, List<SeatEntity> seatEntityList) {
    }
}

