package com.t2pellet.tlib.client;

import com.t2pellet.tlib.TenzinLib;
import com.t2pellet.tlib.client.registry.IModEntityModels;
import com.t2pellet.tlib.client.registry.IModParticleFactories;
import com.t2pellet.tlib.common.TLibMod;

public abstract class TLibModClient {

    public TLibModClient() {
        TLibMod.IMod modInfo = getClass().getAnnotation(TLibMod.IMod.class);
        if (modInfo != null) {
            TenzinLib.INSTANCE.register(modInfo.value(), this);
        }
    }

    public abstract IModEntityModels entityModels();
    public abstract IModParticleFactories particleFactories();

}
