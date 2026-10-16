package ch.sivakumar.softweng.backend.adapter.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

// This record is created and filled with the values in `application.properties`
@ConfigurationProperties(prefix = "mqtt")
public record MqttProperties(Broker broker,
                             Client client,
                             String topic,
                             String username,
                             String password) {

    public record Broker(String url) {
    }

    public record Client(String id) {
    }
}
