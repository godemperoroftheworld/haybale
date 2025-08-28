package com.t2pellet.haybale.common.config.api.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListProperty<T> extends ConfigProperty<List<T>> {

    private final PropertyType<T> type;
    private StringProperty.Validator validator;

    public static ListProperty<String> of(List<String> initialValue, StringProperty.Validator validator) {
        ListProperty<String> stringListProperty = new ListProperty<>(PropertyType.STRING, new ArrayList<>(initialValue));
        stringListProperty.validator = validator;
        return stringListProperty;
    }

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

    @Override
    public void set(List<T> value) {
        if (validator != null) {
            List<T> filteredList = value.stream().filter(val -> validator.validate((String) val)).toList();
            super.set(filteredList);
        } else super.set(value);
    }

    public void setValue(String value) {
        List<String> stringList = Arrays.asList(value.replaceAll("[\\[\\] \" ]", "" ).split("\\s*,\\s*"));
        List<T> typeList = stringList.stream().map(this::convertFromStr).toList();
        set(typeList);
    }

    @SuppressWarnings("unchecked")
    private T convertFromStr(String string) {
        if (type == PropertyType.INT) return (T) (Object) Integer.parseInt(string);
        else if (type == PropertyType.FLOAT) return (T) (Object) Float.parseFloat(string);
        else if (type == PropertyType.BOOL) return (T) (Object) Boolean.parseBoolean(string);
        else return (T) string;
    }
}
