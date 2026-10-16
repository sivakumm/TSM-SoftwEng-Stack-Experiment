package ch.sivakumar.softweng.backend.adapter.websocket;

import ch.sivakumar.softweng.backend.adapter.configurations.WebSocketConfig;
import ch.sivakumar.softweng.backend.adapter.websocket.dto.LiveMeasurementMessage;
import ch.sivakumar.softweng.backend.adapter.websocket.mapper.LiveMeasurementMapper;
import ch.sivakumar.softweng.backend.domain.Measurement;
import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WebSocketLiveMeasurementPublisherTest {

    @Test
    void shouldSendMeasurementAsMessageToMeasurementsTopic() {
        SimpMessagingTemplate messagingTemplateMock = mock();
        LiveMeasurementMapper liveMeasurementMapper = new LiveMeasurementMapper();
        WebSocketLiveMeasurementPublisher publisher = new WebSocketLiveMeasurementPublisher(liveMeasurementMapper, messagingTemplateMock);
        Measurement measurement = new Measurement(Instant.now(), new Temperature(17), null);

        publisher.publish(measurement);

        verify(messagingTemplateMock).convertAndSend(WebSocketConfig.MEASUREMENTS_TOPIC, new LiveMeasurementMessage(measurement.timestamp(), measurement.temperature().value(), null));
    }
}
