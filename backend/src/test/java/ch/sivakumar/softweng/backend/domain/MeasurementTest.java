package ch.sivakumar.softweng.backend.domain;

import ch.sivakumar.softweng.backend.domain.exceptions.SystemException;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementTest {

    private static final Instant VALID_TIMESTAMP = Instant.now().minus(1, ChronoUnit.HOURS);
    private static final Temperature VALID_TEMPERATURE = new Temperature(38.2);
    private static final Humidity VALID_HUMIDITY = new Humidity(55.7);

    @Test
    void shouldThrowWhenTimestampIsMissing() {
        assertThrows(SystemException.class, () -> new Measurement(null, VALID_TEMPERATURE, VALID_HUMIDITY));
    }

    @Test
    void shouldThrowWhenNoMeasurementValueIsGiven() {
        assertThrows(SystemException.class, () -> new Measurement(VALID_TIMESTAMP, null, null));
    }

    @Test
    void shouldCreateMeasurementWhenOnlyTemperatureValueIsGiven() {
        Measurement result = assertDoesNotThrow(() -> new Measurement(VALID_TIMESTAMP, VALID_TEMPERATURE, null));

        assertEquals(VALID_TIMESTAMP, result.timestamp());
        assertEquals(VALID_TEMPERATURE, result.temperature());
    }

    @Test
    void shouldCreateMeasurementWhenOnlyHumidityValueIsGiven() {
        Measurement result = assertDoesNotThrow(() -> new Measurement(VALID_TIMESTAMP, null, VALID_HUMIDITY));

        assertEquals(VALID_TIMESTAMP, result.timestamp());
        assertEquals(VALID_HUMIDITY, result.humidity());
    }

    @Test
    void shouldCreateMeasurementWhenBothValuesAreGiven() {
        Measurement result = assertDoesNotThrow(() -> new Measurement(VALID_TIMESTAMP, VALID_TEMPERATURE, VALID_HUMIDITY));

        assertEquals(VALID_TIMESTAMP, result.timestamp());
        assertEquals(VALID_TEMPERATURE, result.temperature());
        assertEquals(VALID_HUMIDITY, result.humidity());
    }
}
