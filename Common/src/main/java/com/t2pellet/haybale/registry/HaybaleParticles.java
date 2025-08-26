package com.t2pellet.haybale.registry;

import com.t2pellet.haybale.registry.api.ParticleEntryType;
import com.t2pellet.haybale.registry.api.RegistryClass;
import net.minecraft.core.particles.SimpleParticleType;

@RegistryClass.IRegistryClass(SimpleParticleType.class)
public class HaybaleParticles implements RegistryClass {

    @RegistryClass.IRegistryEntry
    public static final ParticleEntryType TEST_PARTICLE = new ParticleEntryType("test");
}
