package com.t2pellet.haybale.config.api.property;


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

    public T get() {
        return value;
    }
    
    public void set(T value) {
        this.value = value;
    }

}
