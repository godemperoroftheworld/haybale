package com.t2pellet.haybale.client.registry;

import com.t2pellet.haybale.client.particle.TestParticle;
import com.t2pellet.haybale.client.registry.api.ParticleFactoryEntryType;
import com.t2pellet.haybale.registry.HaybaleParticles;
import com.t2pellet.haybale.registry.api.RegistryClass;
import net.minecraft.core.particles.ParticleType;

@RegistryClass.IRegistryClass(ParticleType.class)
public class HaybaleParticleFactories implements RegistryClass {

    @RegistryClass.IRegistryEntry
    public static final ParticleFactoryEntryType TEST_PARTICLE = new ParticleFactoryEntryType(HaybaleParticles.TEST_PARTICLE, TestParticle.Factory::new);
}
