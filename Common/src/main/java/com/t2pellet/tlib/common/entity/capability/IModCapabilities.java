package com.t2pellet.tlib.common.entity.capability;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

public interface IModCapabilities {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ICapability {
        String value();
    }

    class TLibCapability<T extends Capability> {
        public final Class<T> clazz;
        Supplier<T> supplier;

        public TLibCapability(Class<T> clazz, Supplier<T> supplier) {
            this.clazz = clazz;
            this.supplier = supplier;
        }

        public T get() {
            return supplier.get();
        }
    }

}
