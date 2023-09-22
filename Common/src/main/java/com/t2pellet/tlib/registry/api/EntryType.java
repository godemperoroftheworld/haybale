package com.t2pellet.tlib.registry.api;

public class EntryType<T> {

    public final Class<T> type;
    private T value;

    protected EntryType(Class<T> type) {
        this.type = type;
    }

    public T get() {
        return value;
    }
}
