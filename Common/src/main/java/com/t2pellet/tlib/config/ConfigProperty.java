package com.t2pellet.tlib.config;


public abstract class ConfigProperty<T> {
    private T value;
    private final T initialValue;

    protected ConfigProperty(T value) {
        this.value = value;
        this.initialValue = value;
    }

    public T getDefault() {
        return initialValue;
    }

    public T getValue() {
        return value;
    }
    
    public void setValue(T value) {
        this.value = value;
    }

}
