package com.t2pellet.tlib.common.registry;

import com.t2pellet.tlib.Services;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.function.Supplier;

public interface IModParticles {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IParticle {
        String value();
    }

    class TLibParticle {
        public final Supplier<SimpleParticleType> PARTICLE = null;
    }
}
