package vortech.test.flight_reservations.domain.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Flight {
    private Long id;
    private String origin;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;
    private List<Seat> seats;
    private List<Reservation> reservations;

    public Flight() {
    }

    public Flight(String origin, String destination, Date departureTime, Date arrivalTime, List<Seat> seats) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seats = seats;
    }
}
