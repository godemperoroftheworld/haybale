package com.t2pellet.haybale.common.registry.api;

import net.minecraft.core.particles.SimpleParticleType;

public class ParticleEntryType extends EntryType<SimpleParticleType>  {

    private final String name;

    public ParticleEntryType(String name) {
        super(SimpleParticleType.class);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
