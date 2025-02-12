package vortech.test.flight_reservations.infraestructure.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AmqpConfiguration {

    public final static String fanoutQueueReservations = "reservations";
    public final static String fanoutQueueDeadLetterReservations = "reservations_dead_letter";
    public final static String fanoutExchangeReservations = "reservations_exchange";

    public final static String fanoutExchangeNotificator = "idap_exchange_notificator";
    

    //region reservations
    @Bean
    public Queue queue_reservations() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", fanoutQueueDeadLetterReservations);
        return new Queue(fanoutQueueReservations, true, false, false, args);
    }

    @Bean
    public Queue queue_reservations_dead_letter() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-message-ttl", 24000);
        args.put("x-dead-letter-routing-key", fanoutQueueReservations);
        return new Queue(fanoutQueueDeadLetterReservations, true, false, false, args);
    }

    @Bean
    public FanoutExchange exchange_reservations() {
        return new FanoutExchange(fanoutExchangeReservations, true, false);
    }

    @Bean
    Binding binding_reservations(Queue queue_reservations, FanoutExchange exchange_reservations) {
        return BindingBuilder.bind(queue_reservations).to(exchange_reservations);
    }
    //endregion

    //region Notification

    @Bean
    public FanoutExchange exchange_notificator() {
        return new FanoutExchange(fanoutExchangeNotificator, true, false);
    }

    //endregion
}

