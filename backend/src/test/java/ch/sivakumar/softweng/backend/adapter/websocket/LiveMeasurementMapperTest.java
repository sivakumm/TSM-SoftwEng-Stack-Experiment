package ch.sivakumar.softweng.backend.adapter.websocket;

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
        Instant timestamp = Instant.now();

        LiveMeasurementMessage result = mapper.toLiveMeasurementMessage(new Measurement(timestamp, new Temperature(17), new Humidity(27)));

        assertEquals(new LiveMeasurementMessage(timestamp, 17.0, 27.0), result);
    }

    @Test
    void shouldMapMissingValuesToNull() {
        Instant timestamp = Instant.now();

        LiveMeasurementMessage result = mapper.toLiveMeasurementMessage(new Measurement(timestamp, null, new Humidity(27)));

        assertNull(result.temperature());
        assertEquals(27.0, result.humidity());
    }
}
