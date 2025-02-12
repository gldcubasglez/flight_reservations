package vortech.test.flight_reservations.infraestructure.adapters.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "flight")
public class FlightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origin;
    private String destination;
    private Date departureTime;
    private Date arrivalTime;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<SeatEntity> seats = new ArrayList<>();

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;

    public FlightEntity() {
    }

    public FlightEntity(String origin, String destination, Date departureTime, Date arrivalTime) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public void addSeat(SeatEntity seat) {
        seats.add(seat);
        seat.setFlight(this);
    }
}



