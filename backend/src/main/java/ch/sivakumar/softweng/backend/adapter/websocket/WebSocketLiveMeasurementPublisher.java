package ch.sivakumar.softweng.backend.adapter.websocket;

import ch.sivakumar.softweng.backend.adapter.configurations.WebSocketConfig;
import ch.sivakumar.softweng.backend.application.LiveMeasurementPublisher;
import ch.sivakumar.softweng.backend.domain.Measurement;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WebSocketLiveMeasurementPublisher implements LiveMeasurementPublisher {

    private final LiveMeasurementMapper liveMeasurementMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void publish(Measurement measurement) {
        LiveMeasurementMessage liveMeasurementMessage = liveMeasurementMapper.toLiveMeasurementMessage(measurement);
        messagingTemplate.convertAndSend(WebSocketConfig.MEASUREMENTS_TOPIC, liveMeasurementMessage);
    }
}
