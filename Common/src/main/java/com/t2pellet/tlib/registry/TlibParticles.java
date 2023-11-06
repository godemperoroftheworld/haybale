package com.t2pellet.tlib.registry;

import com.t2pellet.tlib.registry.api.ParticleEntryType;
import com.t2pellet.tlib.registry.api.RegistryClass;
import net.minecraft.core.particles.SimpleParticleType;

@RegistryClass.IRegistryClass(SimpleParticleType.class)
public class TlibParticles implements RegistryClass {

    @RegistryClass.IRegistryEntry
    public static final ParticleEntryType TEST_PARTICLE = new ParticleEntryType("test");
}
