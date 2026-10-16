package ch.sivakumar.softweng.backend.adapter.rest.measurement.dto;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementResponseTest {

    private static final Instant VALID_TIMESTAMP = Instant.now().minusSeconds(1);
    private static final Double VALID_TEMPERATURE = 38.2;
    private static final Double VALID_HUMIDITY = 24.7;

    @Test
    void shouldThrowWhenTimestampIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> new MeasurementResponse(null, VALID_TEMPERATURE, VALID_HUMIDITY));
    }

    @Test
    void shouldThrowWhenTimestampIsInTheFuture() {
        Instant future = Instant.now().plusSeconds(60);

        assertThrows(IllegalArgumentException.class, () -> new MeasurementResponse(future, VALID_TEMPERATURE, VALID_HUMIDITY));
    }

    @Test
    void shouldThrowWhenNoMeasurementValuesAreGiven() {
        assertThrows(IllegalArgumentException.class, () -> new MeasurementResponse(VALID_TIMESTAMP, null, null));
    }

    @Test
    void shouldCreateWhenOnlyTemperatureValueIsAvailable() {
        MeasurementResponse result = assertDoesNotThrow(() -> new MeasurementResponse(VALID_TIMESTAMP, VALID_TEMPERATURE, null));

        assertEquals(VALID_TIMESTAMP, result.timestamp());
        assertEquals(VALID_TEMPERATURE, result.temperature());
        assertNull(result.humidity());
    }

    @Test
    void shouldCreateWhenOnlyHumidityValueIsAvailable() {
        MeasurementResponse result = assertDoesNotThrow(() -> new MeasurementResponse(VALID_TIMESTAMP, null, VALID_HUMIDITY));

        assertEquals(VALID_TIMESTAMP, result.timestamp());
        assertNull(result.temperature());
        assertEquals(VALID_HUMIDITY, result.humidity());
    }

    @Test
    void shouldCreateWhenBothValuesAreAvailable() {
        MeasurementResponse result = assertDoesNotThrow(() -> new MeasurementResponse(VALID_TIMESTAMP, VALID_TEMPERATURE, VALID_HUMIDITY));

        assertEquals(VALID_TIMESTAMP, result.timestamp());
        assertEquals(VALID_TEMPERATURE, result.temperature());
        assertEquals(VALID_HUMIDITY, result.humidity());
    }
}
