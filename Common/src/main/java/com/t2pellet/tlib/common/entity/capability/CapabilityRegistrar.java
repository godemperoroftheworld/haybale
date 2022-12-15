package com.t2pellet.tlib.common.entity.capability;

import java.util.Optional;
import java.util.function.Supplier;

public interface CapabilityRegistrar {

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
    <T extends Capability> void register(Class<T> cap, Supplier<T> factory);

    /**
     * Instantiates the given capability, if registered
     *
     * @param cap the capability class
     * @param <T> the capability parameter
     * @return an optional for the instance of the capability. Optional is empty if the capability is not registered
     */
    <T extends Capability> Optional<T> get(Class<T> cap);

}
