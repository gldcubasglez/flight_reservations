package vortech.test.flight_reservations.infraestructure.adapters.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "seat")
public class SeatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatNumber;
    private boolean available = true;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    public SeatEntity() {
    }

    public SeatEntity(String seatNumber, FlightEntity flight) {
        this.seatNumber = seatNumber;
        this.flight = flight;
    }
}
