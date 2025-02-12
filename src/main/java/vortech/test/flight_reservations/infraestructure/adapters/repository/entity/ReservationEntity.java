package vortech.test.flight_reservations.infraestructure.adapters.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private Date reservationDate = new Date();
    private String status = "CONFIRMADA";

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<SeatEntity> seats;

    public ReservationEntity() {
    }

    public ReservationEntity(String name, String email, List<SeatEntity> seats, FlightEntity flight) {
        this.name = name;
        this.email = email;
        this.seats = seats;
        this.flight = flight;
    }
}