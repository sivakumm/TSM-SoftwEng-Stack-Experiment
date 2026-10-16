package ch.sivakumar.softweng.backend.adapter.mqtt;

import ch.sivakumar.softweng.backend.application.ReceiveLiveMeasurement;
import ch.sivakumar.softweng.backend.domain.Measurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import tools.jackson.databind.json.JsonMapper;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class MeasurementMqttListenerTest {

    private static final Instant NOW = Instant.parse("2026-10-16T16:00:00Z");

    private ReceiveLiveMeasurement receiveLiveMeasurementMock;

    private MeasurementMqttListener listener;

    @BeforeEach
    void setup() {
        receiveLiveMeasurementMock = mock();
        MqttMeasurementMapper mapper = new MqttMeasurementMapper(Clock.fixed(NOW, ZoneOffset.UTC));
        listener = new MeasurementMqttListener(JsonMapper.builder().build(), mapper, receiveLiveMeasurementMock);
    }

    @Test
    void shouldPassValidPayloadAsMeasurementToUseCase() {
        ArgumentCaptor<Measurement> captor = ArgumentCaptor.forClass(Measurement.class);

        listener.onMessage("{\"temperature\": 17, \"humidity\": 27}");

        verify(receiveLiveMeasurementMock).execute(captor.capture());
        assertEquals(NOW, captor.getValue().timestamp());
        assertEquals(17.0, captor.getValue().temperature().value());
        assertEquals(27.0, captor.getValue().humidity().value());
    }

    @ParameterizedTest
    @ValueSource(strings = {"not json", "{}", "{\"temperature\": 500}", "{\"humidity\": -1}"})
    void shouldDropInvalidPayloadWithoutThrowing(String payload) {
        assertDoesNotThrow(() -> listener.onMessage(payload));

        verify(receiveLiveMeasurementMock, never()).execute(any());
    }
}
