package ch.sivakumar.softweng.backend.adapter.rest.measurement.mapper;

import ch.sivakumar.softweng.backend.adapter.rest.measurement.dto.MeasurementResponse;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MeasurementMapperTest {

    @Test
    void shouldMapMeasurementsToResponseWhenAllFieldsArePresent() {
        Instant timestamp = Instant.now();
        Temperature temperature = new Temperature(25.3);
        Humidity humidity = new Humidity(60.7);
        Measurement measurement = new Measurement(timestamp, temperature, humidity);

        List<MeasurementResponse> result = new MeasurementMapper().toMeasurementResponses(List.of(measurement));

        assertEquals(1, result.size());
        assertEquals(timestamp, result.getFirst().timestamp());
        assertEquals(temperature.value(), result.getFirst().temperature());
        assertEquals(humidity.value(), result.getFirst().humidity());
    }

    @Test
    void shouldMapMeasurementsToResponseWhenWhenTemperatureIsMissing() {
        Instant timestamp = Instant.now();
        Humidity humidity = new Humidity(60.7);
        Measurement measurement = new Measurement(timestamp, null, humidity);

        List<MeasurementResponse> result = new MeasurementMapper().toMeasurementResponses(List.of(measurement));

        assertEquals(1, result.size());
        assertEquals(timestamp, result.getFirst().timestamp());
        assertNull(result.getFirst().temperature());
        assertEquals(humidity.value(), result.getFirst().humidity());
    }

    @Test
    void shouldMapMeasurementsToResponseWhenWhenHumidityIsMissing() {
        Instant timestamp = Instant.now();
        Temperature temperature = new Temperature(25.3);
        Measurement measurement = new Measurement(timestamp, temperature, null);

        List<MeasurementResponse> result = new MeasurementMapper().toMeasurementResponses(List.of(measurement));

        assertEquals(1, result.size());
        assertEquals(timestamp, result.getFirst().timestamp());
        assertEquals(temperature.value(), result.getFirst().temperature());
        assertNull(result.getFirst().humidity());
    }
}
