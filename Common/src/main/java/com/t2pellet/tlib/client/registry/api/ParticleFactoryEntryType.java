package com.t2pellet.tlib.client.registry.api;

import com.t2pellet.tlib.registry.api.EntryType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class ParticleFactoryEntryType<T extends ParticleOptions> extends EntryType<ParticleType> {

    private final Function<SpriteSet, ParticleProvider<T>> providerFunction;

    protected ParticleFactoryEntryType(Function<SpriteSet, ParticleProvider<T>> providerFunction) {
        super(ParticleType.class);
        this.providerFunction = providerFunction;
    }

    public Function<SpriteSet, ParticleProvider<T>> getProviderFunction() {
        return providerFunction;
    }
}
