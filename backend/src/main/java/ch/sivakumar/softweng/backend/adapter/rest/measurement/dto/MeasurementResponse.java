package ch.sivakumar.softweng.backend.adapter.rest.measurement.dto;

import java.time.Instant;

public record MeasurementResponse(Instant timestamp,
                                  Double temperature,
                                  Double humidity) {
}
