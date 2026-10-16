package ch.sivakumar.softweng.backend.adapter.mqtt.dto;

public record MqttMeasurementPayload(Double temperature,
                                     Double humidity) {
}
