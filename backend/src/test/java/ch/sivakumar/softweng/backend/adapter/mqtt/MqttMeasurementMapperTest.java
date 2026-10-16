package ch.sivakumar.softweng.backend.adapter.mqtt;

import ch.sivakumar.softweng.backend.adapter.mqtt.dto.MqttMeasurementPayload;
import ch.sivakumar.softweng.backend.adapter.mqtt.mapper.MqttMeasurementMapper;
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
        MqttMeasurementPayload payload = new MqttMeasurementPayload(17.0, 27.0);

        Measurement result = mapper.toMeasurement(payload);

        assertEquals(NOW, result.timestamp());
        assertEquals(payload.temperature(), result.temperature().value());
        assertEquals(payload.humidity(), result.humidity().value());
    }

    @Test
    void shouldMapPayloadWithOnlyTemperature() {
        MqttMeasurementPayload payload = new MqttMeasurementPayload(17.0, null);

        Measurement result = mapper.toMeasurement(payload);

        assertEquals(payload.temperature(), result.temperature().value());
        assertNull(result.humidity());
    }

    @Test
    void shouldMapPayloadWithOnlyHumidity() {
        MqttMeasurementPayload payload = new MqttMeasurementPayload(null, 27.0);

        Measurement result = mapper.toMeasurement(payload);

        assertNull(result.temperature());
        assertEquals(payload.humidity(), result.humidity().value());
    }

    @Test
    void shouldThrowWhenPayloadHasNoValues() {
        MqttMeasurementPayload payload = new MqttMeasurementPayload(null, null);

        assertThrows(SystemException.class, () -> mapper.toMeasurement(payload));
    }
}
