package com.t2pellet.tlib.client.particle;

import com.t2pellet.tlib.client.registry.api.ParticleFactoryEntryType;
import com.t2pellet.tlib.registry.TlibParticles;
import com.t2pellet.tlib.registry.api.RegistryClass;
import net.minecraft.core.particles.ParticleType;

@RegistryClass.IRegistryClass(ParticleType.class)
public class TlibParticleFactories implements RegistryClass {

    @RegistryClass.IRegistryEntry
    public static final ParticleFactoryEntryType TEST_PARTICLE = new ParticleFactoryEntryType(TlibParticles.TEST_PARTICLE, TestParticle.Factory::new);
}
