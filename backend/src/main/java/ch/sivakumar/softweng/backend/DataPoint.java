package ch.sivakumar.softweng.backend;

import java.time.Instant;

public record DataPoint(Instant time, Object value) {
}
