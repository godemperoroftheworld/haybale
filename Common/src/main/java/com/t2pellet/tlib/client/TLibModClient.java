package com.t2pellet.tlib.client;

import com.t2pellet.tlib.client.registry.IModEntityModels;
import com.t2pellet.tlib.client.registry.IModParticleFactories;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public interface TLibModClient {

    // Client
    IModEntityModels entityModels();
    IModParticleFactories particleFactories();

}
