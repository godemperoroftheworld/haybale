package com.t2pellet.tlib.entity.capability.registry;

import com.t2pellet.tlib.entity.capability.api.Capability;
import com.t2pellet.tlib.entity.capability.api.ICapabilityHaver;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class CapabilityRegistrarImpl implements CapabilityRegistrar {

    private final Map<Class<?>, CapabilityFactory<?>> capabilityFactoryMap = new HashMap<>();

    @Override
    public <T extends Capability> void register(Class<T> cap, CapabilityFactory<T> factory) {
        capabilityFactoryMap.put(cap, factory);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Capability, E extends Entity & ICapabilityHaver> Optional<T> get(Class<T> cap, E entity) {
        CapabilityFactory<T> factory = (CapabilityFactory<T>) capabilityFactoryMap.get(cap);
        if (factory != null) {
            return Optional.of(factory.get(entity));
        }
        return Optional.empty();
    }

}
