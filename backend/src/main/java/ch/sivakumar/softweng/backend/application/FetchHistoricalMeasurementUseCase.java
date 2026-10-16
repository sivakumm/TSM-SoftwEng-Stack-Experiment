package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.application.ports.in.FetchHistoricalMeasurement;
import ch.sivakumar.softweng.backend.application.ports.out.HistoricalMeasurementRepository;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.OpenTimeRange;
import ch.sivakumar.softweng.backend.domain.types.PastTimeRange;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FetchHistoricalMeasurementUseCase implements FetchHistoricalMeasurement {

    private final Clock clock;
    private final HistoricalMeasurementRepository historicalMeasurementRepository;

    @Override
    public List<Measurement> execute(OpenTimeRange openTimeRange) {
        PastTimeRange queryTimeRange = getQueryTimeRange(openTimeRange);
        return historicalMeasurementRepository.findMeasurements(queryTimeRange);
    }

    private PastTimeRange getQueryTimeRange(OpenTimeRange openTimeRange) {
        Instant maximumPastTimeRangeStart = twoDaysAgo();
        Instant maximumPastTimeRangeEnd = clock.instant();

        if (openTimeRange == null) {
            return new PastTimeRange(maximumPastTimeRangeStart, maximumPastTimeRangeEnd);
        }

        Instant timeRangeStart = getValidTimeRangeStart(openTimeRange, maximumPastTimeRangeStart);
        Instant timeRangeEnd = getValidTimeRangeEnd(openTimeRange, maximumPastTimeRangeEnd);
        return new PastTimeRange(timeRangeStart, timeRangeEnd);
    }

    private Instant twoDaysAgo() {
        return clock.instant().minus(2L, ChronoUnit.DAYS);
    }

    private static Instant getValidTimeRangeStart(OpenTimeRange openTimeRange, Instant maximumPastTimeRangeStart) {
        return Optional.ofNullable(openTimeRange.start())
                .filter(rangeStart -> !rangeStart.isBefore(maximumPastTimeRangeStart))
                .orElse(maximumPastTimeRangeStart);
    }

    private static Instant getValidTimeRangeEnd(OpenTimeRange openTimeRange, Instant maximumPastTimeRangeEnd) {
        return Optional.ofNullable(openTimeRange.end())
                .filter(rangeEnd -> !rangeEnd.isAfter(maximumPastTimeRangeEnd))
                .orElse(maximumPastTimeRangeEnd);
    }
}
