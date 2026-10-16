package ch.sivakumar.softweng.backend.application.ports.out;

import ch.sivakumar.softweng.backend.domain.Measurement;

public interface LiveMeasurementPublisher {

    void publish(Measurement measurement);
}
