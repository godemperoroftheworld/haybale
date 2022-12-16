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
        Class<? extends Capability> value();
    }

    class TLibCapability<T extends Capability> {
        Supplier<T> supplier;

        public TLibCapability(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        public T get() {
            return supplier.get();
        }
    }

}
