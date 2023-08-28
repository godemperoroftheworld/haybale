package com.t2pellet.tlib.common.entity.capability;

import net.minecraft.world.level.entity.EntityAccess;

import java.util.Optional;

public interface CapabilityRegistrar {

    @FunctionalInterface
    interface CapabilityFactory<T extends Capability, E extends ICapabilityHaver & EntityAccess> {
        T get(E entity);
    }

    /**
     * The capability handler instance. Use this to register and instantiate your capabilities
     */
    CapabilityRegistrar INSTANCE = new CapabilityRegistrarImpl();

    /**
     * Registers the given capability.
     *
     * @param cap     : the capability class
     * @param factory : the capability factory
     * @param <T>     the capability parameter
     */
    <T extends Capability, E extends ICapabilityHaver & EntityAccess> void register(Class<T> cap, CapabilityFactory<T, E> factory);

    /**
     * Instantiates the given capability, if registered
     *
     * @param cap the capability class
     * @param <T> the capability parameter
     * @return an optional for the instance of the capability. Optional is empty if the capability is not registered
     */
    <T extends Capability, E extends ICapabilityHaver & EntityAccess> Optional<T> get(Class<T> cap, E entity);

}
