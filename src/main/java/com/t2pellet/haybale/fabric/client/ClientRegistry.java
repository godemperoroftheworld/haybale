//? if fabric {
/*package com.t2pellet.haybale.fabric.client;

import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.client.registry.IClientRegistry;
import com.t2pellet.haybale.client.registry.api.EntityModelEntryType;
import com.t2pellet.haybale.client.registry.api.EntityRendererEntryType;
import com.t2pellet.haybale.client.registry.api.ParticleFactoryEntryType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.function.Supplier;

public class ClientRegistry implements IClientRegistry {

    @Override
    public Supplier<ModelLayerLocation> register(String modid, EntityModelEntryType modelEntry) {
        ModelLayerLocation loc = new ModelLayerLocation(Services.VERSION_HELPER.getResourceLocation(modid, modelEntry.getName()), "main");
        EntityModelLayerRegistry.registerModelLayer(loc, modelEntry::getLayerDefinition);
        return () -> loc;
    }

    @Override
    public <T extends Entity> Supplier<EntityRendererProvider<T>> register(String modid, EntityRendererEntryType<T> rendererEntry) {
        EntityRendererRegistry.register(rendererEntry.getEntityType(), rendererEntry.getRendererProvider());
        return rendererEntry::getRendererProvider;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Supplier<ParticleType<SimpleParticleType>> register(String modid, ParticleFactoryEntryType particleFactoryEntry) {
        ParticleFactoryRegistry.getInstance().register(particleFactoryEntry.get(), particleFactoryEntry.getProviderFunction()::apply);
        return particleFactoryEntry::get;
    }
}
*///?}