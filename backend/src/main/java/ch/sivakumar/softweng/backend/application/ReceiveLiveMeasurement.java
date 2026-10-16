package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.domain.Measurement;

public interface ReceiveLiveMeasurement {

    void execute(Measurement measurement);
}
