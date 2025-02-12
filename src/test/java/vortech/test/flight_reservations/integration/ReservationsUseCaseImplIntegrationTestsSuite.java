package vortech.test.flight_reservations.integration;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({SeatsUseCaseImplIntegrationTests.class,
        CancellationUseCaseImplIntegrationTests.class,
        ReservationsUseCaseImplIntegrationTests.class,
        MakeReservationsUseCaseImplIntegrationTests.class})
public class ReservationsUseCaseImplIntegrationTestsSuite {
}
