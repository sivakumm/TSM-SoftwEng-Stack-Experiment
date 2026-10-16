package ch.sivakumar.softweng.backend.adapter.rest.measurement.mapper;

import ch.sivakumar.softweng.backend.adapter.rest.measurement.dto.MeasurementResponse;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MeasurementMapper {

    public List<MeasurementResponse> toMeasurementResponses(List<Measurement> measurements) {
        return measurements.stream()
                .map(MeasurementMapper::toMeasurementResponse)
                .toList();
    }

    private static MeasurementResponse toMeasurementResponse(Measurement measurement) {
        Double temperature = Optional.ofNullable(measurement.temperature()).map(Temperature::value).orElse(null);
        Double humidity = Optional.ofNullable(measurement.humidity()).map(Humidity::value).orElse(null);
        return new MeasurementResponse(measurement.timestamp(), temperature, humidity);
    }
}
