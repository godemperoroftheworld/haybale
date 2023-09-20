package com.t2pellet.tlib.config.property;

import com.t2pellet.tlib.config.ConfigProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListProperty<T> extends ConfigProperty<List<T>> {

    private final PropertyType<T> type;

    public static <R> ListProperty<R> of(PropertyType<R> type, List<R> initialValue) {
        if (type == PropertyType.BOOL) throw new IllegalArgumentException("No support for bool lists. Sorry");
        return new ListProperty<>(type, new ArrayList<>(initialValue));
    }

    public PropertyType<T> getType() {
        return type;
    }

    private ListProperty(PropertyType<T> type, List<T> value) {
        super(value);
        this.type = type;
    }

    public void setValue(String value) {
        List<String> stringList = Arrays.asList(value.replaceAll("[\\[\\] \" ]", "" ).split("\\s*,\\s*"));
        List<T> typeList = stringList.stream().map(this::convertFromStr).toList();
        setValue(typeList);
    }

    @SuppressWarnings("unchecked")
    private T convertFromStr(String string) {
        if (type == PropertyType.INT) return (T) (Object) Integer.parseInt(string);
        else if (type == PropertyType.FLOAT) return (T) (Object) Float.parseFloat(string);
        else if (type == PropertyType.BOOL) return (T) (Object) Boolean.parseBoolean(string);
        else return (T) string;
    }
}
