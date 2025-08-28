package com.t2pellet.haybale.common.registry;

import com.t2pellet.haybale.common.registry.api.ParticleEntryType;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import net.minecraft.core.particles.SimpleParticleType;

@RegistryClass.IRegistryClass(SimpleParticleType.class)
public class HaybaleParticles implements RegistryClass {

    @IRegistryEntry
    public static final ParticleEntryType TEST_PARTICLE = new ParticleEntryType("test");
}
