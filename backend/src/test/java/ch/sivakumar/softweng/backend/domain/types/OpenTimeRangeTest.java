package ch.sivakumar.softweng.backend.domain.types;

import ch.sivakumar.softweng.backend.domain.exception.ApplicationException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class OpenTimeRangeTest {

    private static final Instant VALID_START = Instant.now().minus(24, ChronoUnit.HOURS);
    private static final Instant VALID_END = Instant.now().plus(24, ChronoUnit.HOURS);

    @Test
    void shouldCreateWhenNoStartOrEndIsGiven() {
        OpenTimeRange result = new OpenTimeRange(null, null);

        assertNotNull(result);
        assertNull(result.start());
        assertNull(result.end());
    }

    @Test
    void shouldCreateWhenNoStartIsGiven() {
        OpenTimeRange result = new OpenTimeRange(null, VALID_END);

        assertNotNull(result);
        assertNull(result.start());
        assertEquals(VALID_END, result.end());
    }

    @Test
    void shouldCreateWhenNoEndIsGiven() {
        OpenTimeRange result = new OpenTimeRange(VALID_START, null);

        assertNotNull(result);
        assertEquals(VALID_START, result.start());
        assertNull(result.end());
    }

    @Test
    void shouldThrowWhenStartIsAfterEnd() {
        Instant start = Instant.now().plusSeconds(100);
        Instant end = Instant.now().minusSeconds(100);

        assertThrows(ApplicationException.class, () -> new OpenTimeRange(start, end));
    }

    @Test
    void shouldCreateWhenStartIsBeforeEnd() {
        OpenTimeRange result = new OpenTimeRange(VALID_START, VALID_END);

        assertNotNull(result);
        assertEquals(VALID_START, result.start());
        assertEquals(VALID_END, result.end());
    }
}
