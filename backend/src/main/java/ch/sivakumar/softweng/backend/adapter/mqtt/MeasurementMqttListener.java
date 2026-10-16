package ch.sivakumar.softweng.backend.adapter.mqtt;

import ch.sivakumar.softweng.backend.adapter.configurations.MqttConfig;
import ch.sivakumar.softweng.backend.adapter.mqtt.dto.MqttMeasurementPayload;
import ch.sivakumar.softweng.backend.adapter.mqtt.mapper.MqttMeasurementMapper;
import ch.sivakumar.softweng.backend.application.ports.in.ReceiveLiveMeasurement;
import ch.sivakumar.softweng.backend.domain.exception.SystemException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Component
@AllArgsConstructor
public class MeasurementMqttListener {

    private static final Logger log = LoggerFactory.getLogger(MeasurementMqttListener.class);

    private final JsonMapper jsonMapper;
    private final MqttMeasurementMapper mqttMeasurementMapper;
    private final ReceiveLiveMeasurement receiveLiveMeasurement;

    @ServiceActivator(inputChannel = MqttConfig.MQTT_INPUT_CHANNEL)
    public void onMessage(String payload) {
        try {
            MqttMeasurementPayload measurementPayload = jsonMapper.readValue(payload, MqttMeasurementPayload.class);
            receiveLiveMeasurement.execute(mqttMeasurementMapper.toMeasurement(measurementPayload));
        } catch (JacksonException | SystemException exception) {
            log.warn("Dropping invalid MQTT measurement '{}': {}", payload, exception.getMessage());
        }
    }
}
