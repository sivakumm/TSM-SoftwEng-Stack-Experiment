package ch.sivakumar.softweng.backend.domain;

import ch.sivakumar.softweng.backend.domain.exception.SystemException;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;

import java.time.Instant;

public record Measurement(Instant timestamp,
                          Temperature temperature,
                          Humidity humidity) {

    public Measurement {
        validateTimestampIsGiven(timestamp);
        validateMeasurementDataIsGiven(temperature, humidity);
    }

    private static void validateTimestampIsGiven(Instant timestamp) {
        if (timestamp == null) {
            throw new SystemException("Expect every Measurement to have a valid Timestamp");
        }
    }

    private static void validateMeasurementDataIsGiven(Temperature temperature, Humidity humidity) {
        if (temperature == null && humidity == null) {
            throw new SystemException("Either Temperature or Humidity value must be given for a valid Measurement");
        }
    }
}
