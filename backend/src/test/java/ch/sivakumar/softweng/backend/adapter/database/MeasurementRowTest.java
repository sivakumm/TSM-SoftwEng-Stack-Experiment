package ch.sivakumar.softweng.backend.adapter.database;

import ch.sivakumar.softweng.backend.domain.Measurement;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MeasurementRowTest {

    private static final Instant VALID_TIMESTAMP = Instant.now();
    private static final Double VALID_TEMPERATURE = 38.2;
    private static final Double VALID_HUMIDITY = 65.0;

    @Test
    void shouldCreateValidMeasurementDomainObjectWhenAllMeasurementsAreFilled() {
        MeasurementRow row = MeasurementRow.builder()
                .time(VALID_TIMESTAMP)
                .temperature(VALID_TEMPERATURE)
                .humidity(VALID_HUMIDITY)
                .build();

        Optional<Measurement> result = row.toDomain();

        assertTrue(result.isPresent());
        assertEquals(VALID_TIMESTAMP, result.get().timestamp());
        assertEquals(VALID_TEMPERATURE, result.get().temperature().value());
        assertEquals(VALID_HUMIDITY, result.get().humidity().value());
    }

    @Test
    void shouldCreateValidMeasurementDomainObjectWhenTemperatureIsNotFilled() {
        MeasurementRow row = MeasurementRow.builder()
                .time(VALID_TIMESTAMP)
                .temperature(null)
                .humidity(VALID_HUMIDITY)
                .build();

        Optional<Measurement> result = row.toDomain();

        assertTrue(result.isPresent());
        assertEquals(VALID_TIMESTAMP, result.get().timestamp());
        assertNull(result.get().temperature());
        assertEquals(VALID_HUMIDITY, result.get().humidity().value());
    }

    @Test
    void shouldCreateValidMeasurementDomainObjectWhenHumidityIsNotFilled() {
        MeasurementRow row = MeasurementRow.builder()
                .time(VALID_TIMESTAMP)
                .temperature(VALID_TEMPERATURE)
                .humidity(null)
                .build();

        Optional<Measurement> result = row.toDomain();

        assertTrue(result.isPresent());
        assertEquals(VALID_TIMESTAMP, result.get().timestamp());
        assertEquals(VALID_TEMPERATURE, result.get().temperature().value());
        assertNull(result.get().humidity());
    }

    @Test
    void shouldCreateOptionalEmptyWhenDomainObjectCouldNotBeCreated() {
        MeasurementRow row = MeasurementRow.builder()
                .time(null)
                .temperature(VALID_TEMPERATURE)
                .humidity(VALID_HUMIDITY)
                .build();

        Optional<Measurement> result = row.toDomain();

        assertTrue(result.isEmpty());
    }
}
