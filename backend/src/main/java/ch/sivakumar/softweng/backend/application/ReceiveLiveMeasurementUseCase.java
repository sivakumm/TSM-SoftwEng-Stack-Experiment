package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.domain.Measurement;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReceiveLiveMeasurementUseCase implements ReceiveLiveMeasurement {

    private final LiveMeasurementPublisher liveMeasurementPublisher;

    @Override
    public void execute(Measurement measurement) {
        liveMeasurementPublisher.publish(measurement);
    }
}
