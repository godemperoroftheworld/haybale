package com.t2pellet.tlib.client.registry.api;

import com.t2pellet.tlib.registry.api.EntryType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class ParticleFactoryEntryType<T extends ParticleOptions> extends EntryType<ParticleType> {

    private final ParticleType<T> particleType;
    private final Function<SpriteSet, ParticleProvider<T>> providerFunction;

    public ParticleFactoryEntryType(ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> providerFunction) {
        super(ParticleType.class);
        this.particleType = particleType;
        this.providerFunction = providerFunction;
    }

    @Override
    public ParticleType<T> get() {
        return particleType;
    }

    public Function<SpriteSet, ParticleProvider<T>> getProviderFunction() {
        return providerFunction;
    }
}
