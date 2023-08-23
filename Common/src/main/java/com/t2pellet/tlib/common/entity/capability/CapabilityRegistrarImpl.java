package com.t2pellet.tlib.common.entity.capability;

import net.minecraft.world.level.entity.EntityAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

class CapabilityRegistrarImpl implements CapabilityRegistrar {

    private final Map<Class<?>, CapabilityFactory<?, ?>> capabilityFactoryMap = new HashMap<>();

    @Override
    public <T extends Capability, E extends ICapabilityHaver & EntityAccess> void register(Class<T> cap, CapabilityFactory<T, E> factory) {
        capabilityFactoryMap.put(cap, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Capability> Optional<T> get(Class<T> cap) {
        Supplier<T> factory = (Supplier<T>) capabilityFactoryMap.get(cap);
        if (factory != null) {
            return Optional.of(factory.get());
        }
        return Optional.empty();
    }

}
