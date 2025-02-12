package vortech.test.flight_reservations;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.logging.LogManager;

@EnableJpaRepositories
@SpringBootApplication
public class FlightReservationsApplication {
	static {
		try {
			// Cargar la configuración de logging desde el archivo
			LogManager.getLogManager().readConfiguration(
					FlightReservationsApplication.class.getResourceAsStream("/logging.properties")
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(FlightReservationsApplication.class, args);
	}

}

