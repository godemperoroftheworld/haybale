package com.t2pellet.tlib.client.compat;

import com.t2pellet.tlib.config.Config;
import com.t2pellet.tlib.config.ConfigRegistrar;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ConfigMenu {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface IConfigMenu {
        String modid();
    }

    final String modid;
    final Config config;

    // needs to be public for fabric ModMenu integration
    public ConfigMenu() {
        IConfigMenu menuInfo = getClass().getAnnotation(IConfigMenu.class);
        this.modid = menuInfo.modid();
        this.config = ConfigRegistrar.INSTANCE.get(this.modid);
    }

    void setField(Field field, Object value) {
        try {
            field.set(null, value);
            config.save();
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    boolean isStringList(Field field) {
        Type listType = field.getGenericType();
        if (listType instanceof ParameterizedType parameterizedType) {
            Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            return String.class.isAssignableFrom(type);
        }
        return false;
    }

}
