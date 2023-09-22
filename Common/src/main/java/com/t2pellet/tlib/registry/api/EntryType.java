package com.t2pellet.tlib.registry.api;

import java.util.function.Supplier;

public class EntryType<T> implements Supplier<T> {

    public final Class<T> type;
    private T value;

    protected EntryType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        return value;
    }
}
