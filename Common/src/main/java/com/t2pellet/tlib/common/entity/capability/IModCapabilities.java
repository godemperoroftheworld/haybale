package com.t2pellet.tlib.common.entity.capability;

import net.minecraft.world.level.entity.EntityAccess;

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

    class TLibCapability<T extends Capability, E extends ICapabilityHaver & EntityAccess> {
        CapabilityRegistrar.CapabilityFactory<T, E> supplier;

        public TLibCapability(CapabilityRegistrar.CapabilityFactory<T, E> supplier) {
            this.supplier = supplier;
        }

        public T get(E entity) {
            return supplier.get(entity);
        }
    }

}
