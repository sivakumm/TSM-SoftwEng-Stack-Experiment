package ch.sivakumar.softweng.backend.adapter.rest.measurement.dto;

import java.time.Instant;

public record MeasurementResponse(Instant timestamp,
                                  Double temperature,
                                  Double humidity) {

    public MeasurementResponse {
        validateTimestamp(timestamp);
        validateMeasurementValues(temperature, humidity);
    }

    private static void validateTimestamp(Instant timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        if (timestamp.isAfter(Instant.now())) {
            throw new IllegalArgumentException("Timestamp cannot be in the future");
        }
    }

    private static void validateMeasurementValues(Double temperature, Double humidity) {
        if (temperature == null && humidity == null) {
            throw new IllegalArgumentException("Measurement must hold at least one value");
        }
    }
}
