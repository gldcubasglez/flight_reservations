package vortech.test.flight_reservations.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Reservation {
    private Long id;
    private String name;
    private String email;
    private Date reservationDate = new Date();
    private String status = "CONFIRMADA";
    private Flight flight;
    private List<Seat> seats;

    public Reservation() {
    }

    public Reservation(String name, String email, List<Seat> seats, Flight flight) {
        this.name = name;
        this.email = email;
        this.seats = seats;
        this.flight = flight;
    }
}