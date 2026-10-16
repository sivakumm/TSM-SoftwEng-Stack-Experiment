package ch.sivakumar.softweng.backend.adapter.mqtt;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.util.Optional;

@Component
@AllArgsConstructor
public class MqttMeasurementMapper {

    private final Clock clock;

    public Measurement toMeasurement(MqttMeasurementPayload payload) {
        Temperature temperature = Optional.ofNullable(payload.temperature()).map(Temperature::new).orElse(null);
        Humidity humidity = Optional.ofNullable(payload.humidity()).map(Humidity::new).orElse(null);
        return new Measurement(clock.instant(), temperature, humidity);
    }
}
