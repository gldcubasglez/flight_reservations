package vortech.test.flight_reservations.infraestructure.config;

import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    private static final String DESCRIPTION = """
            Mediante la siguiente API se podrán realizar las siguientes operaciones: <br>
            <ol>
               <li>Generar datos de vuelos y asientos predeterminados.</li>
               <li>Realizar una reservación.</li>
               <li>Cancelar una reservación.</li>
               <li>Listar los asientos disponibles de un vuelo.</li>
               <li>Listar las reservaciones confirmadas.</li>
           </ol>
           <br><br>
           <b>Datos técnicos:</b><br>
           <ul>
               <li>Java 21</li>
               <li>Spring Boot 3</li>
               <li>PostgresSQL</li>
               <li>JPA + Hibernate</li>
               <li>RabbitMQ</li>
               <li>OpenApi / Swagger</li>
           </ul>
            """;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API para reservaciones de vuelos")
                        .version("1.0")
                        .description(DESCRIPTION));
    }
}
