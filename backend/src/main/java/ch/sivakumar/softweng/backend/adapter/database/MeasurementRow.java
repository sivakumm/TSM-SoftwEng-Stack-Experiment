package ch.sivakumar.softweng.backend.adapter.database;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import com.influxdb.annotations.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeasurementRow {

    static final String FIELD_TEMPERATURE = "temperature";
    static final String FIELD_HUMIDITY = "humidity";

    @Column(timestamp = true)
    private Instant time;

    @Column(name = FIELD_TEMPERATURE)
    private Double temperature;

    @Column(name = FIELD_HUMIDITY)
    private Double humidity;

    Optional<Measurement> toDomain() {
        try {
            Temperature domainTemperature = temperature != null ? new Temperature(temperature) : null;
            Humidity domainHumidity = humidity != null ? new Humidity(humidity) : null;
            Measurement measurement = new Measurement(time, domainTemperature, domainHumidity);
            return Optional.of(measurement);
        } catch (Exception _) {
            return Optional.empty();
        }
    }
}
