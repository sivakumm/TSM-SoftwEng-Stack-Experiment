package ch.sivakumar.softweng.backend.domain.types;

public record Temperature(double value) {

    private static final double MINIMUM_VALID_TEMPERATURE_VALUE = -40;
    private static final double MAXIMUM_VALID_TEMPERATURE_VALUE = 100;

    public Temperature {
        validate(value);
    }

    private static void validate(double value) {
        if (value < MINIMUM_VALID_TEMPERATURE_VALUE) {
            throw new IllegalArgumentException("The value shouldn't be less than " + MINIMUM_VALID_TEMPERATURE_VALUE + ". Received " + value);
        }

        if (value > MAXIMUM_VALID_TEMPERATURE_VALUE) {
            throw new IllegalArgumentException("The value shouldn't be greater than " + MAXIMUM_VALID_TEMPERATURE_VALUE + ". Received " + value);
        }
    }
}
