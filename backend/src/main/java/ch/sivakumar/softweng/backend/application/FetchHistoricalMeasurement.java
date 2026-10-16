package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.OpenTimeRange;

import java.util.List;

public interface FetchHistoricalMeasurement {

    List<Measurement> execute(OpenTimeRange openTimeRange);
}
