package ch.sivakumar.softweng.backend.domain.types;

import ch.sivakumar.softweng.backend.domain.exceptions.ApplicationException;

import java.time.Instant;

public record OpenTimeRange(Instant start, Instant end) {

    public OpenTimeRange {
        validateStartIsBeforeEnd(start, end);
    }

    private static void validateStartIsBeforeEnd(Instant start, Instant end) {
        if (start == null || end == null) {
            return;
        }

        if (start.isAfter(end)) {
            throw new ApplicationException("Expected start to be before end. Start: " + start + ", End: " + end);
        }
    }
}
