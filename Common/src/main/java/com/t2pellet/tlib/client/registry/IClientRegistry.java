package com.t2pellet.tlib.client.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IClientRegistry {

    <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> aNew);

    ModelLayerLocation registerEntityRenderer(String modid,
                                                   String name,
                                                   EntityRendererProvider<? extends Entity> renderSupplier,
                                                   Supplier<ModelLayerLocation> model,
                                                   LayerDefinition modelData);

}
