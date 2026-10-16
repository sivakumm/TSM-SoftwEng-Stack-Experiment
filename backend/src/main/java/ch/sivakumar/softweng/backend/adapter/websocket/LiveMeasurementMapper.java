package ch.sivakumar.softweng.backend.adapter.websocket;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LiveMeasurementMapper {

    public LiveMeasurementMessage toLiveMeasurementMessage(Measurement measurement) {
        Double temperature = Optional.ofNullable(measurement.temperature()).map(Temperature::value).orElse(null);
        Double humidity = Optional.ofNullable(measurement.humidity()).map(Humidity::value).orElse(null);
        return new LiveMeasurementMessage(measurement.timestamp(), temperature, humidity);
    }
}
