package com.t2pellet.tlib.io;

public interface StringConverter {

    static StringConverter of(String value) {
        return new StringConverterImpl(value);
    }

    <T> T convert(Class<T> clazz);

}
