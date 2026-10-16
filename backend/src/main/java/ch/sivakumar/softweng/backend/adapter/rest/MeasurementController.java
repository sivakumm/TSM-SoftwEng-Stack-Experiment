package ch.sivakumar.softweng.backend.adapter.rest;

import ch.sivakumar.softweng.backend.adapter.rest.dto.MeasurementResponse;
import ch.sivakumar.softweng.backend.application.FetchHistoricalMeasurement;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.exceptions.ApplicationException;
import ch.sivakumar.softweng.backend.domain.types.OpenTimeRange;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
@AllArgsConstructor
public class MeasurementController {

    private final FetchHistoricalMeasurement fetchHistoricalMeasurement;
    private final MeasurementMapper measurementMapper;

    @GetMapping
    public List<MeasurementResponse> getMeasurements(@RequestParam(required = false) String start, @RequestParam(required = false) String end) {
        OpenTimeRange timeRange = new OpenTimeRange(instantFromString(start), instantFromString(end));
        List<Measurement> measurements = fetchHistoricalMeasurement.execute(timeRange);
        return measurementMapper.toMeasurementResponses(measurements);
    }

    private static Instant instantFromString(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Instant.parse(value);
        } catch (Exception exception) {
            throw new ApplicationException("Given value is not a valid timestamp. Value: " + value, exception);
        }
    }
}
