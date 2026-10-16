package ch.sivakumar.softweng.backend.domain;

import ch.sivakumar.softweng.backend.domain.types.Humidity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class HumidityTest {

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, 100.01})
    void shouldThrowWhenValueIsOutOfValidRange(double humidityValue) {
        assertThrows(IllegalArgumentException.class, () -> new Humidity(humidityValue));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 38.2, 100.0})
    void shouldCreateHumidityInValidRange(double humidityValue) {
        Humidity result = assertDoesNotThrow(() -> new Humidity(humidityValue));

        assertEquals(humidityValue, result.value());
    }
}
