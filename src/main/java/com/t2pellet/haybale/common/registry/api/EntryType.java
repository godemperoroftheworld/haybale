package com.t2pellet.haybale.common.registry.api;

import java.util.function.Supplier;

public class EntryType<T> implements Supplier<T> {

    public final Class<T> type;
    private Supplier<T> supplier;

    protected EntryType(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        return supplier.get();
    }
}
