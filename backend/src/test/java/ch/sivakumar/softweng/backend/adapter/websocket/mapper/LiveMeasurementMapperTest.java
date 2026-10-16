package ch.sivakumar.softweng.backend.adapter.websocket.mapper;

import ch.sivakumar.softweng.backend.adapter.websocket.dto.LiveMeasurementMessage;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LiveMeasurementMapperTest {

    private final LiveMeasurementMapper mapper = new LiveMeasurementMapper();

    @Test
    void shouldMapAllValues() {
        Measurement measurement = new Measurement(Instant.now(), new Temperature(17), new Humidity(27));

        LiveMeasurementMessage result = mapper.toLiveMeasurementMessage(measurement);

        assertEquals(measurement.timestamp(), result.timestamp());
        assertEquals(measurement.temperature().value(), result.temperature());
        assertEquals(measurement.humidity().value(), result.humidity());
    }

    @Test
    void shouldMapMissingTemperatureToNull() {
        Measurement measurement = new Measurement(Instant.now(), null, new Humidity(27));

        LiveMeasurementMessage result = mapper.toLiveMeasurementMessage(measurement);

        assertEquals(measurement.timestamp(), result.timestamp());
        assertNull(result.temperature());
        assertEquals(measurement.humidity().value(), result.humidity());
    }

    @Test
    void shouldMapMissingHumidityToNull() {
        Measurement measurement = new Measurement(Instant.now(), new Temperature(17), null);

        LiveMeasurementMessage result = mapper.toLiveMeasurementMessage(measurement);

        assertEquals(measurement.timestamp(), result.timestamp());
        assertEquals(measurement.temperature().value(), result.temperature());
        assertNull(result.humidity());
    }
}
