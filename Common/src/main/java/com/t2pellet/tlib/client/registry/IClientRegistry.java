package com.t2pellet.tlib.client.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

public interface IClientRegistry {

    <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> aNew);

    void registerModelLayer(ModelLayerLocation modelLayerLocation, LayerDefinition modelData);

    <T extends Entity> void registerEntityRenderer(EntityType<T> type,
                                                   EntityRendererProvider<T> renderSupplier);

}
