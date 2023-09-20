package com.t2pellet.tlib.config.property;

import com.t2pellet.tlib.config.ConfigProperty;

public class IntProperty extends ConfigProperty<Integer> {

    private final int minValue;
    private final int maxValue;

    public IntProperty(Integer value, int minValue, int maxValue) {
        super(value);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public IntProperty(Integer value) {
        super(value);
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
    }

    @Override
    public void setValue(Integer value) {
        if (minValue <= value && value <= maxValue) {
            super.setValue(value);
        }
    }
}
