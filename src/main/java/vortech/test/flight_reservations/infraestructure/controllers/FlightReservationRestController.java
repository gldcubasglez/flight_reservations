package vortech.test.flight_reservations.infraestructure.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vortech.test.flight_reservations.application.services.FlightReservationService;
import vortech.test.flight_reservations.domain.model.Reservation;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FlightReservationRestController {

    //    http://localhost:8080/public/swagger-ui/index.html

    private static final String VALID_REQUEST_RESERVATION = """
            {
              "name": "string",
              "email": "abc@abc.com",
              "flightId": "1",
              "seatId": "1"
            }""";


    private final FlightReservationService flightReservationService;

    public FlightReservationRestController(FlightReservationService flightReservationService) {
        this.flightReservationService = flightReservationService;
    }

    @Operation(summary = "Listado de asientos disponibles",
            description = "Se obtiene un listado de asientos disponibles dado su id. Si el id no existe se devuelve la lista vacía.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Su vuelo ha sido cancelado satifactoriamente")
    })
    @GetMapping(value = "/public/flight/{id}/seats", produces = "application/json")
    public ResponseEntity<?> checkAvailableFlightSeats(@Parameter(description = "Id del vuelo", required = true)
                                                       @PathVariable Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            Object response = flightReservationService.checkAvailableFlightSeats(id);
            result.put("success", true);
            result.put("result", response);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Genera datos predeterminados",
            description = "Genera datos de vuelos y asientos predeterminados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se han generado los datos satifactoriamente.")
    })
    @GetMapping(value = "/api/generate_default_data", produces = "application/json")
    public ResponseEntity<?> generateDefaultData() {

        Map<String, Object> result = new HashMap<>();
        try {
            Object response = flightReservationService.generateDefaultData();
            result.put("success", true);
            result.put("result", response);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Realizar una reservación",
            description = "Se realiza una reserva aportando los datos necesarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Su solicitud se está procesando."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping(value = "/api/reservation", produces = "application/json")
    public ResponseEntity<?> makeReservation(@Parameter(description = "Datos de la reservación", required = true)
                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                     content = @Content(
                                                             schema = @Schema(implementation = Reservation.class),
                                                             examples = {
                                                                     @ExampleObject(
                                                                             name = "Se deben introducir todos los datos",
                                                                             summary = "Datos del objeto",
                                                                             value = VALID_REQUEST_RESERVATION
                                                                     )
                                                             }))
                                             @RequestBody Reservation request) {

        Map<String, Object> result = new HashMap<>();
        try {
            Object response = flightReservationService.makeReservation(request);
            result.put("success", true);
            result.put("result", response);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (BadRequestException e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Cancelar una reservación",
            description = "Se cancela una reservación dado su id. Se verifica si el id existe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Su vuelo ha sido cancelado satifactoriamente")
    })
    @DeleteMapping(value = "/api/reservation/{id}", produces = "application/json")
    public ResponseEntity<?> cancelReservation(@Parameter(description = "Id de la reservación", required = true)
                                               @PathVariable Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            Object response = flightReservationService.cancelReservation(id);
            result.put("success", true);
            result.put("result", response);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (BadRequestException e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Listado de reservaciones",
            description = "Se obtiene el listado de reservaciones confirmadas dado su id. Si el id no existe se obtiene una lista vacía.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Su vuelo ha sido cancelado satifactoriamente")
    })
    @GetMapping(value = "/public/reservation/{id}", produces = "application/json")
    public ResponseEntity<?> getConfirmedReservations(@Parameter(description = "Id de la reservación", required = true)
                                                      @PathVariable Long id) {

        Map<String, Object> result = new HashMap<>();
        try {
            Object response = flightReservationService.getConfirmedReservations(id);
            result.put("success", true);
            result.put("result", response);
            return new ResponseEntity<Object>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.put("success", false);
            result.put("result", e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
