package com.t2pellet.haybale.client;

import com.t2pellet.haybale.registry.api.RegistryClass;

public abstract class HaybaleModClient {

    public Class<? extends RegistryClass> entityModels() { return null; }

    public Class<? extends RegistryClass> entityRenderers() { return null; }

    public Class<? extends RegistryClass> particleFactories() { return null; }

}
