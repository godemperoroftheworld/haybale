package com.t2pellet.haybale.client;

import com.t2pellet.haybale.client.registry.HaybaleEntityRenderers;
import com.t2pellet.haybale.client.registry.HaybaleParticleFactories;
import com.t2pellet.haybale.common.registry.api.RegistryClass;

public class HaybaleClient extends HaybaleModClient {

    public static final HaybaleClient INSTANCE = new HaybaleClient();

    @Override
    public Class<? extends RegistryClass> particleFactories() {
        return HaybaleParticleFactories.class;
    }

    @Override
    public Class<? extends RegistryClass> entityRenderers() {
        return HaybaleEntityRenderers.class;
    }
}
