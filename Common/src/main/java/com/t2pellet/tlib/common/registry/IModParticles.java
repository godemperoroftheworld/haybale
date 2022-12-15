package com.t2pellet.tlib.common.registry;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

public interface IModParticles {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IParticle {
        String value();
    }

    class TLibParticle<T extends ParticleOptions> {
        public final Supplier<ParticleType<T>> PARTICLE = null;
    }
}
