package ch.sivakumar.softweng.backend.adapter.mqtt;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.exception.SystemException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class MqttMeasurementMapperTest {

    private static final Instant NOW = Instant.parse("2026-10-16T16:00:00Z");

    private final MqttMeasurementMapper mapper = new MqttMeasurementMapper(Clock.fixed(NOW, ZoneOffset.UTC));

    @Test
    void shouldMapPayloadWithBothValuesAndStampReceptionTime() {
        Measurement result = mapper.toMeasurement(new MqttMeasurementPayload(17.0, 27.0));

        assertEquals(NOW, result.timestamp());
        assertEquals(17.0, result.temperature().value());
        assertEquals(27.0, result.humidity().value());
    }

    @Test
    void shouldMapPayloadWithOnlyTemperature() {
        Measurement result = mapper.toMeasurement(new MqttMeasurementPayload(17.0, null));

        assertEquals(17.0, result.temperature().value());
        assertNull(result.humidity());
    }

    @Test
    void shouldMapPayloadWithOnlyHumidity() {
        Measurement result = mapper.toMeasurement(new MqttMeasurementPayload(null, 27.0));

        assertNull(result.temperature());
        assertEquals(27.0, result.humidity().value());
    }

    @Test
    void shouldThrowWhenPayloadHasNoValues() {
        MqttMeasurementPayload payload = new MqttMeasurementPayload(null, null);

        assertThrows(SystemException.class, () -> mapper.toMeasurement(payload));
    }

    @Test
    void shouldThrowWhenValueIsOutOfRange() {
        MqttMeasurementPayload payload = new MqttMeasurementPayload(150.0, 27.0);

        assertThrows(SystemException.class, () -> mapper.toMeasurement(payload));
    }
}
