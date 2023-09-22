package com.t2pellet.tlib.client;

import com.t2pellet.tlib.registry.api.RegistryClass;

public abstract class TLibModClient {

    public Class<? extends RegistryClass> entityModels() { return null; }

    public Class<? extends RegistryClass> entityRenderers() { return null; }

    public Class<? extends RegistryClass> particleFactories() { return null; }

}
