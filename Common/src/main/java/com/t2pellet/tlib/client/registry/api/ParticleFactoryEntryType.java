package com.t2pellet.tlib.client.registry.api;

import com.t2pellet.tlib.registry.api.EntryType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class ParticleFactoryEntryType<T extends ParticleOptions> extends EntryType<ParticleType> {

    private final String name;
    private final ParticleType<T> particleType;
    private final Function<SpriteSet, ParticleProvider<T>> providerFunction;

    public ParticleFactoryEntryType(String name, ParticleType<T> particleType, Function<SpriteSet, ParticleProvider<T>> providerFunction) {
        super(ParticleType.class);
        this.name = name;
        this.particleType = particleType;
        this.providerFunction = providerFunction;
    }

    @Override
    public ParticleType<T> get() {
        return particleType;
    }

    public String getName() {
        return name;
    }

    public Function<SpriteSet, ParticleProvider<T>> getProviderFunction() {
        return providerFunction;
    }
}
