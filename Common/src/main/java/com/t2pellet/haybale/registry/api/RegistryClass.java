package com.t2pellet.haybale.registry.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface RegistryClass {
    // Annotate the class with this. Pass in the registry type.
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface IRegistryClass {
        Class<?> value();
    }

    // For entries in a IRegistryClass annotated class
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface IRegistryEntry {
    }
}
