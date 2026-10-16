package ch.sivakumar.softweng.backend.adapter.websocket;

import ch.sivakumar.softweng.backend.adapter.configurations.WebSocketConfig;
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
        WebSocketLiveMeasurementPublisher publisher = new WebSocketLiveMeasurementPublisher(messagingTemplateMock, new LiveMeasurementMapper());
        Instant timestamp = Instant.now();

        publisher.publish(new Measurement(timestamp, new Temperature(17), null));

        verify(messagingTemplateMock).convertAndSend(WebSocketConfig.MEASUREMENTS_TOPIC, new LiveMeasurementMessage(timestamp, 17.0, null));
    }
}
