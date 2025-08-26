package com.t2pellet.haybale.client.registry.api;

import com.t2pellet.haybale.registry.api.EntryType;
import com.t2pellet.haybale.registry.api.ParticleEntryType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Function;

public class ParticleFactoryEntryType extends EntryType<ParticleType> {

    private final ParticleEntryType particleEntry;
    private final Function<SpriteSet, ParticleProvider<SimpleParticleType>> providerFunction;

    public ParticleFactoryEntryType(ParticleEntryType particleEntry, Function<SpriteSet, ParticleProvider<SimpleParticleType>> providerFunction) {
        super(ParticleType.class);
        this.particleEntry = particleEntry;
        this.providerFunction = providerFunction;
    }

    @Override
    public ParticleType<SimpleParticleType> get() {
        return particleEntry.get();
    }

    public String getName() {
        return particleEntry.getName();
    }

    public Function<SpriteSet, ParticleProvider<SimpleParticleType>> getProviderFunction() {
        return providerFunction;
    }
}
