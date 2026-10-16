package ch.sivakumar.softweng.backend.application;

import ch.sivakumar.softweng.backend.application.ports.out.LiveMeasurementPublisher;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Humidity;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ReceiveLiveMeasurementUseCaseTest {

    @Test
    void shouldPublishReceivedMeasurement() {
        LiveMeasurementPublisher liveMeasurementPublisherMock = mock();
        Measurement measurement = new Measurement(Instant.now(), new Temperature(17), new Humidity(27));
        ReceiveLiveMeasurementUseCase useCase = new ReceiveLiveMeasurementUseCase(liveMeasurementPublisherMock);

        useCase.execute(measurement);

        verify(liveMeasurementPublisherMock).publish(measurement);
    }
}
