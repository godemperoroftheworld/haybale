package com.t2pellet.haybale.common.config.api.property;

public class FloatProperty extends ConfigProperty<Float> {
    private final float minValue;
    private final float maxValue;

    public FloatProperty(float value, float minValue, float maxValue) {
        super(value);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public FloatProperty(float value) {
        super(value);
        this.minValue = Float.MIN_VALUE;
        this.maxValue = Float.MAX_VALUE;
    }

    @Override
    public void set(Float value) {
        if (minValue <= value && value <= maxValue) {
            super.set(value);
        }
    }
}
