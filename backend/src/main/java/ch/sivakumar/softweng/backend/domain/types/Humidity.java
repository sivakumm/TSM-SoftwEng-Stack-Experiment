package ch.sivakumar.softweng.backend.domain.types;

import ch.sivakumar.softweng.backend.domain.exception.SystemException;

public record Humidity(double value) {

    private static final double MINIMUM_VALID_HUMIDITY_VALUE = 0;
    private static final double MAXIMUM_VALID_HUMIDITY_VALUE = 100;

    public Humidity {
        validate(value);
    }

    private static void validate(double value) {
        if (value < MINIMUM_VALID_HUMIDITY_VALUE) {
            throw new SystemException("The value shouldn't be less than " + MINIMUM_VALID_HUMIDITY_VALUE + ". Received " + value);
        }

        if (value > MAXIMUM_VALID_HUMIDITY_VALUE) {
            throw new SystemException("The value shouldn't be greater than " + MAXIMUM_VALID_HUMIDITY_VALUE + ". Received " + value);
        }
    }
}
