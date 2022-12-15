package com.t2pellet.tlib.client.registry;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IModParticleFactories {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IParticleFactory {
    }

    class TLibParticleFactory<T extends ParticleOptions> {
        public final Supplier<SimpleParticleType> _supplier;
        public final Function<SpriteSet, ParticleProvider<T>> _provider;

        public TLibParticleFactory(Supplier<SimpleParticleType> supplier, Function<SpriteSet, ParticleProvider<T>> provider) {
            this._supplier = supplier;
            this._provider = provider;
        }
    }
}
