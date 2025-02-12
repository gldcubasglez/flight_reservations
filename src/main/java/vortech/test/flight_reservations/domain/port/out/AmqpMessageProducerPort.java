package vortech.test.flight_reservations.domain.port.out;

import java.io.IOException;
import java.util.Map;

public interface AmqpMessageProducerPort {
	
	void sendMessagesReservation(Map<String, Object> message) throws IOException;

	void sendMessagesNotification(Map<String, Object> message) throws IOException;

}
