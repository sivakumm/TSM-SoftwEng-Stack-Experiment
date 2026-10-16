package ch.sivakumar.softweng.backend.adapter.mqtt;

public record MqttMeasurementPayload(Double temperature,
                                     Double humidity) {
}
