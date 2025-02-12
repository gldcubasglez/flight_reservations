package vortech.test.flight_reservations.unit;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({SeatsUseCaseImplTests.class,
        CancellationUseCaseImplTests.class,
        ReservationsUseCaseImplTests.class,
        MakeReservationsUseCaseImplTests.class})
public class TestsSuite {
}
