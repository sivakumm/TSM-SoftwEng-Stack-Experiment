package ch.sivakumar.softweng.backend.domain.types;

import ch.sivakumar.softweng.backend.domain.exceptions.ApplicationException;

import java.time.Instant;

public record PastTimeRange(Instant start, Instant end) {

    public PastTimeRange {
        validateNonNull(start);
        validateNonNull(end);
        validateIsInPast(start);
        validateIsInPast(end);
        validateStartIsBeforeEnd(start, end);
    }

    private static void validateNonNull(Instant instant) {
        if (instant == null) {
            throw new ApplicationException("Start and end must be provided, but was null");
        }
    }

    private static void validateIsInPast(Instant instant) {
        Instant now = Instant.now();
        if (instant.isAfter(now)) {
            throw new ApplicationException("Time Range must be in the past. Given: " + instant + ", now: " + now);
        }
    }

    private static void validateStartIsBeforeEnd(Instant start, Instant end) {
        if (start.isAfter(end)) {
            throw new ApplicationException("Expected start to be before end. Start: " + start + ", End: " + end);
        }
    }
}
