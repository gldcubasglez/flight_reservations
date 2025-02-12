package vortech.test.flight_reservations.application.usecases;

import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import vortech.test.flight_reservations.domain.port.in.CancellationUseCase;
import vortech.test.flight_reservations.infraestructure.adapters.repository.ReservationRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.SeatRepository;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.ReservationEntity;
import vortech.test.flight_reservations.infraestructure.adapters.repository.entity.SeatEntity;

import java.util.List;

@Service
public class CancellationUseCaseImpl implements CancellationUseCase {
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    public CancellationUseCaseImpl(SeatRepository seatRepository, ReservationRepository reservationRepository) {
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Override
    public String cancelReservation(Long reservationId) throws BadRequestException {
        try {
            validateData(reservationId);

            ReservationEntity reservationEntity = validateRepositoryData(reservationId);
            reservationEntity.setStatus("CANCELADA");
            reservationRepository.save(reservationEntity);

            List<SeatEntity> seats = reservationEntity.getSeats();
            for (SeatEntity seatEntity : seats) {
                seatEntity.setAvailable(true);
            }

            seatRepository.saveAll(seats);

            return "Su vuelo ha sido cancelado satifactoriamente.";
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateData(Long reservationId) throws BadRequestException {
        if (reservationId == null || reservationId.equals(0L))
            throw new BadRequestException("El id de la reservación es requerido.");
    }

    private ReservationEntity validateRepositoryData(Long reservationId) throws BadRequestException {
        ReservationEntity reservationEntity = reservationRepository.findByReservationId(reservationId);

        if (reservationEntity == null)
            throw new BadRequestException("La reservación no existe.");
        return reservationEntity;
    }
}
