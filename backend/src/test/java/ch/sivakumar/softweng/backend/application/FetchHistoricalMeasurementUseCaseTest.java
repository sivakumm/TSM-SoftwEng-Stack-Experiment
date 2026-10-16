package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.domain.types.OpenTimeRange;
import ch.sivakumar.softweng.backend.domain.types.PastTimeRange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FetchHistoricalMeasurementUseCaseTest {

    private HistoricalMeasurementRepository historicalMeasurementRepositoryMock;

    private FetchHistoricalMeasurementUseCase useCase;

    private ArgumentCaptor<PastTimeRange> timeRangeArgumentCaptor;

    private static boolean isWithinOneMinute(Instant instant, Instant reference) {
        return instant.isAfter(reference.minusSeconds(30))
                && instant.isBefore(reference.plusSeconds(30));
    }

    @BeforeEach
    void setup() {
        timeRangeArgumentCaptor = ArgumentCaptor.forClass(PastTimeRange.class);
        historicalMeasurementRepositoryMock = mock();

        useCase = new FetchHistoricalMeasurementUseCase(historicalMeasurementRepositoryMock);
    }

    @Test
    void shouldQueryMeasurementDataWithMaximumAllowedTimeRangeWhenNoTimeRangeIsGiven() {
        Instant twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS);
        Instant now = Instant.now();

        useCase.execute(null);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), twoDaysAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), now));
    }

    @Test
    void shouldQueryMeasurementDataWithMaximumAllowedTimeRangeWhenNeitherStartNorEndIsGiven() {
        Instant twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS);
        Instant now = Instant.now();
        OpenTimeRange timeRange = new OpenTimeRange(null, null);

        useCase.execute(timeRange);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), twoDaysAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), now));
    }

    @Test
    void shouldQueryMeasurementDataFromTwoDaysAgoWhenNoStartIsGiven() {
        Instant twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS);
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        OpenTimeRange timeRange = new OpenTimeRange(null, oneDayAgo);

        useCase.execute(timeRange);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), twoDaysAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), oneDayAgo));
    }

    @Test
    void shouldQueryMeasurementDataTillNowWhenNoEndIsGiven() {
        Instant now = Instant.now();
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        OpenTimeRange timeRange = new OpenTimeRange(oneDayAgo, null);

        useCase.execute(timeRange);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), oneDayAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), now));
    }

    @Test
    void shouldQueryMeasurementDataFromTwoDaysAgoEvenWhenGivenStartIsBeforeTwoDaysAgo() {
        Instant threeDaysAgo = Instant.now().minus(3, ChronoUnit.DAYS);
        Instant twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS);
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        OpenTimeRange timeRange = new OpenTimeRange(threeDaysAgo, oneDayAgo);

        useCase.execute(timeRange);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), twoDaysAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), oneDayAgo));
    }

    @Test
    void shouldQueryMeasurementDataTillNowEvenWhenGivenEndIsInFuture() {
        Instant twoDaysAgo = Instant.now().minus(2, ChronoUnit.DAYS);
        Instant now = Instant.now();
        Instant future = Instant.now().plus(1, ChronoUnit.DAYS);
        OpenTimeRange timeRange = new OpenTimeRange(twoDaysAgo, future);

        useCase.execute(timeRange);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), twoDaysAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), now));
    }

    @Test
    void shouldQueryMeasurementDataInGivenRangeIfItFullfillsAllowedRangeLimits() {
        Instant oneDayAgo = Instant.now().minus(1, ChronoUnit.DAYS);
        Instant halfDayAgo = Instant.now().minus(12, ChronoUnit.HOURS);
        OpenTimeRange timeRange = new OpenTimeRange(oneDayAgo, halfDayAgo);

        useCase.execute(timeRange);

        verify(historicalMeasurementRepositoryMock).findMeasurements(timeRangeArgumentCaptor.capture());
        assertNotNull(timeRangeArgumentCaptor.getValue());
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().start(), oneDayAgo));
        assertTrue(isWithinOneMinute(timeRangeArgumentCaptor.getValue().end(), halfDayAgo));
    }
}
