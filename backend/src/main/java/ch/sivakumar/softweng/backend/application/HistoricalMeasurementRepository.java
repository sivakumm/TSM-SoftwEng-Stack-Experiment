package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.PastTimeRange;

import java.util.List;

public interface HistoricalMeasurementRepository {

    List<Measurement> findMeasurements(PastTimeRange timeRange);
}
