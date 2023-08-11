package com.t2pellet.tlib.client;

import com.t2pellet.tlib.client.registry.IModEntityModels;
import com.t2pellet.tlib.client.registry.IModEntityRenderers;
import com.t2pellet.tlib.client.registry.IModParticleFactories;

public abstract class TLibModClient {

    public IModEntityModels entityModels() { return null; }

    public IModEntityRenderers entityRenderers() { return null; }

    public IModParticleFactories particleFactories() { return null; }

}
