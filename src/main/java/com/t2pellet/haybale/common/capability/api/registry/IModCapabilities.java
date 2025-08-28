package com.t2pellet.haybale.common.capability.api.registry;

import com.t2pellet.haybale.common.capability.api.Capability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import com.t2pellet.haybale.common.capability.registry.CapabilityRegistrar;
import net.minecraft.world.entity.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IModCapabilities {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ICapability {
        Class<? extends Capability> value();
    }

    class haybaleCapability<T extends Capability> {
        CapabilityRegistrar.CapabilityFactory<T> supplier;

        public haybaleCapability(CapabilityRegistrar.CapabilityFactory<T> supplier) {
            this.supplier = supplier;
        }

        public <E extends Entity & ICapabilityHaver> T get(E entity) {
            return supplier.get(entity);
        }
    }

}
