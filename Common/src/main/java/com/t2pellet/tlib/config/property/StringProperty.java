package com.t2pellet.tlib.config.property;

import com.t2pellet.tlib.config.ConfigProperty;

public class StringProperty extends ConfigProperty<String> {

    @FunctionalInterface
    public interface Validator {
        boolean validate(String value);
    }

    private final Validator validator;

    public StringProperty(String value, Validator validator) {
        super(value);
        this.validator = validator;
    }

    public StringProperty(String value) {
        super(value);
        this.validator = null;
    }

    @Override
    public void setValue(String value) {
        if (validator == null || validator.validate(value)) {
            super.setValue(value);
        }
    }
}
