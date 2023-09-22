package com.t2pellet.tlib.client.registry;

import com.t2pellet.tlib.client.registry.api.EntityModelEntryType;
import com.t2pellet.tlib.client.registry.api.EntityRendererEntryType;
import com.t2pellet.tlib.client.registry.api.ParticleFactoryEntryType;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IClientRegistry {

    Supplier<ParticleType<SimpleParticleType>> register(String modid, ParticleFactoryEntryType particleFactoryEntry);
    Supplier<ModelLayerLocation> register(String modid, EntityModelEntryType modelEntry);
    <T extends Entity> Supplier<EntityRendererProvider<T>> register(String modid, EntityRendererEntryType<T> rendererEntry);

}
