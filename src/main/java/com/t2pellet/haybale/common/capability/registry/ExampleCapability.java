package com.t2pellet.haybale.common.capability.registry;

import com.t2pellet.haybale.common.capability.api.Capability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import net.minecraft.world.entity.Entity;

public interface ExampleCapability extends Capability {

    static <E extends Entity & ICapabilityHaver> ExampleCapability getInstance(E entity) {
        return new ExampleCapabilityImpl<>(entity);
    }

    int count();

    void decrement();
}
