package vortech.test.flight_reservations.infraestructure.amqp;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vortech.test.flight_reservations.domain.port.out.AmqpMessageProducerPort;

import java.io.IOException;
import java.util.Map;


@Component
public class AmqpMessageProducer implements AmqpMessageProducerPort {

    private final RabbitTemplate rabbitTemplate;
    protected final AmqpTemplate amqpTemplate;

    @Autowired
    public AmqpMessageProducer(RabbitTemplate rabbitTemplate, AmqpTemplate amqpTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessagesReservation(Map<String, Object> message) throws IOException {
        rabbitTemplate.convertAndSend(AmqpConfiguration.fanoutExchangeReservations, "", newMessage(message));
    }

    public void sendMessagesNotification(Map<String, Object> message) throws IOException {
        rabbitTemplate.convertAndSend(AmqpConfiguration.fanoutExchangeNotificator, "", newMessage(message));
    }

    protected Message newMessage(Object obj) throws IOException {
        return new Message(convertToJsonBytes(obj), new MessageProperties());
    }

    private static byte[] convertToJsonBytes(Object obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Para manejar fechas
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Configuración de fechas
        return objectMapper.writeValueAsBytes(obj); // Serializa el objeto a byte[]
    }
}
