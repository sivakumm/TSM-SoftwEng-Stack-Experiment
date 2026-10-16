package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.application.ports.out.HistoricalMeasurementRepository;
import ch.sivakumar.softweng.backend.domain.types.OpenTimeRange;
import ch.sivakumar.softweng.backend.domain.types.PastTimeRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FetchHistoricalMeasurementUseCaseTest {

    private static final Instant NOW = Instant.parse("2026-01-15T12:00:00Z");
    private static final Instant HALF_DAY_AGO = NOW.minus(12, ChronoUnit.HOURS);
    private static final Instant ONE_DAY_AGO = NOW.minus(1, ChronoUnit.DAYS);
    private static final Instant TWO_DAYS_AGO = NOW.minus(2, ChronoUnit.DAYS);
    private static final Instant THREE_DAYS_AGO = NOW.minus(3, ChronoUnit.DAYS);
    private static final Instant ONE_DAY_IN_FUTURE = NOW.plus(1, ChronoUnit.DAYS);

    private HistoricalMeasurementRepository historicalMeasurementRepositoryMock;

    private FetchHistoricalMeasurementUseCase useCase;

    private ArgumentCaptor<PastTimeRange> timeRangeArgumentCaptor;

    private void assertQueriedTimeRange(Instant expectedStart, Instant expectedEnd) {
        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertEquals(expectedStart, timeRangeArgumentCaptor.getValue().start());
        assertEquals(expectedEnd, timeRangeArgumentCaptor.getValue().end());
    }

    @BeforeEach
    void setup() {
        Clock clockMock = mock();
        when(clockMock.instant()).thenReturn(NOW);

        historicalMeasurementRepositoryMock = mock();
        timeRangeArgumentCaptor = ArgumentCaptor.forClass(PastTimeRange.class);

        useCase = new FetchHistoricalMeasurementUseCase(clockMock, historicalMeasurementRepositoryMock);
    }

    @Test
    void shouldQueryMeasurementDataWithMaximumAllowedTimeRangeWhenNoTimeRangeIsGiven() {
        useCase.execute(null);

        assertQueriedTimeRange(TWO_DAYS_AGO, NOW);
    }

    @Test
    void shouldQueryMeasurementDataWithMaximumAllowedTimeRangeWhenNeitherStartNorEndIsGiven() {
        useCase.execute(new OpenTimeRange(null, null));

        assertQueriedTimeRange(TWO_DAYS_AGO, NOW);
    }

    @Test
    void shouldQueryMeasurementDataFromTwoDaysAgoWhenNoStartIsGiven() {
        useCase.execute(new OpenTimeRange(null, ONE_DAY_AGO));

        assertQueriedTimeRange(TWO_DAYS_AGO, ONE_DAY_AGO);
    }

    @Test
    void shouldQueryMeasurementDataTillNowWhenNoEndIsGiven() {
        useCase.execute(new OpenTimeRange(ONE_DAY_AGO, null));

        assertQueriedTimeRange(ONE_DAY_AGO, NOW);
    }

    @Test
    void shouldQueryMeasurementDataFromTwoDaysAgoEvenWhenGivenStartIsBeforeTwoDaysAgo() {
        useCase.execute(new OpenTimeRange(THREE_DAYS_AGO, ONE_DAY_AGO));

        assertQueriedTimeRange(TWO_DAYS_AGO, ONE_DAY_AGO);
    }

    @Test
    void shouldQueryMeasurementDataTillNowEvenWhenGivenEndIsInFuture() {
        useCase.execute(new OpenTimeRange(TWO_DAYS_AGO, ONE_DAY_IN_FUTURE));

        assertQueriedTimeRange(TWO_DAYS_AGO, NOW);
    }

    @Test
    void shouldQueryMeasurementDataInGivenRangeIfItFullfillsAllowedRangeLimits() {
        useCase.execute(new OpenTimeRange(ONE_DAY_AGO, HALF_DAY_AGO));

        assertQueriedTimeRange(ONE_DAY_AGO, HALF_DAY_AGO);
    }
}
