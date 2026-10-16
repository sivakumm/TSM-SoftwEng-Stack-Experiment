package ch.sivakumar.softweng.backend.adapter.websocket;

import java.time.Instant;

public record LiveMeasurementMessage(Instant timestamp,
                                     Double temperature,
                                     Double humidity) {
}
