package ch.sivakumar.softweng.backend.domain;

import ch.sivakumar.softweng.backend.domain.types.Temperature;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureTest {

    @ParameterizedTest
    @ValueSource(doubles = {-40.01, 100.01})
    void shouldThrowWhenValueIsOutOfValidRange(double temperatureValue) {
        assertThrows(IllegalArgumentException.class, () -> new Temperature(temperatureValue));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-40.0, 38.2, 100.0})
    void shouldCreateTemperatureInValidRange(double temperatureValue) {
        Temperature result = assertDoesNotThrow(() -> new Temperature(temperatureValue));

        assertEquals(temperatureValue, result.value());
    }
}
