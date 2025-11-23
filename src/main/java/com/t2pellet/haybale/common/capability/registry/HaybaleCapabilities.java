package com.t2pellet.haybale.common.capability.registry;

import com.t2pellet.haybale.common.capability.api.registry.IModCapabilities;

public class HaybaleCapabilities implements IModCapabilities {

    @ICapability(ExampleCapability.class)
    public static final HaybaleCapability<ExampleCapability> exampleCapability = new HaybaleCapability<>(ExampleCapability::getInstance);
}
