package com.t2pellet.haybale.client.registry;

import com.t2pellet.haybale.client.registry.api.EntityModelEntryType;
import com.t2pellet.haybale.client.registry.api.EntityRendererEntryType;
import com.t2pellet.haybale.client.registry.api.ParticleFactoryEntryType;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public interface IClientRegistry {

    Supplier<ParticleType<SimpleParticleType>> register(String modid, ParticleFactoryEntryType particleFactoryEntry);
    Supplier<ModelLayerLocation> register(String modid, EntityModelEntryType modelEntry);
    <T extends Entity> Supplier<EntityRendererProvider<T>> register(String modid, EntityRendererEntryType<T> rendererEntry);

}
