package ch.sivakumar.softweng.backend.domain.types;

import ch.sivakumar.softweng.backend.domain.exception.ApplicationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PastTimeRangeTest {

    private static final Instant VALID_START = Instant.now().minus(24, ChronoUnit.HOURS);
    private static final Instant VALID_END = Instant.now().minus(12, ChronoUnit.HOURS);

    @Test
    void shouldThrowWhenStartIsNotGiven() {
        assertThrows(ApplicationException.class, () -> new PastTimeRange(null, VALID_END));
    }

    @Test
    void shouldThrowWhenEndIsNotGiven() {
        assertThrows(ApplicationException.class, () -> new PastTimeRange(VALID_START, null));
    }

    @Test
    void shouldThrowWhenStartIsNotInFuture() {
        Instant future = Instant.now().plusSeconds(10);

        assertThrows(ApplicationException.class, () -> new PastTimeRange(future, VALID_END));
    }

    @Test
    void shouldThrowWhenEndIsNotInFuture() {
        Instant future = Instant.now().plusSeconds(10);

        assertThrows(ApplicationException.class, () -> new PastTimeRange(VALID_START, future));
    }

    @Test
    void shouldThrowWhenStartIsAfterEnd() {
        Instant start = Instant.now().minusSeconds(100);
        Instant end = Instant.now().minusSeconds(200);

        assertThrows(ApplicationException.class, () -> new PastTimeRange(start, end));
    }

    @Test
    void shouldCreateForValidTimeRangeInThePast() {
        PastTimeRange result = new PastTimeRange(VALID_START, VALID_END);

        assertEquals(VALID_START, result.start());
        assertEquals(VALID_END, result.end());
    }
}
