package vortech.test.flight_reservations.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Seat {
    private Long id;
    private String seatNumber;
    private boolean available = true;
    private Flight flight;
    private Reservation reservation;

    public Seat() {
    }

    public Seat(String seatNumber, Flight flight) {
        this.seatNumber = seatNumber;
        this.flight = flight;
    }
}
