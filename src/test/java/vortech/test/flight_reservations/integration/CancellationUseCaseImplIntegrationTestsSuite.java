package vortech.test.flight_reservations.integration;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CancellationUseCaseImplIntegrationTests.class})
public class CancellationUseCaseImplIntegrationTestsSuite {
}
