package com.t2pellet.haybale.common.config.api.property;

public class PropertyType<T> {
    public static final PropertyType<String> STRING = new PropertyType<>(String.class);
    public static final PropertyType<Integer> INT = new PropertyType<>(Integer.class);
    public static final PropertyType<Boolean> BOOL = new PropertyType<>(Boolean.class);
    public static final PropertyType<Float> FLOAT = new PropertyType<>(Float.class);

    private PropertyType(Class<T> clazz) {}
}
