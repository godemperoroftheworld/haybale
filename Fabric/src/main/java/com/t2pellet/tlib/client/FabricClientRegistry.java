package com.t2pellet.tlib.client;

import com.t2pellet.tlib.client.registry.IClientRegistry;
import com.t2pellet.tlib.client.registry.api.EntityModelEntryType;
import com.t2pellet.tlib.client.registry.api.EntityRendererEntryType;
import com.t2pellet.tlib.client.registry.api.ParticleFactoryEntryType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricClientRegistry implements IClientRegistry {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ParticleOptions> Supplier<ParticleType<T>> register(String modid, ParticleFactoryEntryType<T> particleFactoryEntry) {
        ClientSpriteRegistryCallback.registerBlockAtlas((atlasTexture, registry) -> {
            registry.register(new ResourceLocation(modid, "particle/" + particleFactoryEntry.getName()));
        });
        ParticleFactoryRegistry.getInstance().register(particleFactoryEntry.get(), particleFactoryEntry.getProviderFunction()::apply);
        return particleFactoryEntry::get;
    }

    @Override
    public Supplier<ModelLayerLocation> register(String modid, EntityModelEntryType modelEntry) {
        ModelLayerLocation loc = new ModelLayerLocation(new ResourceLocation(modid, modelEntry.getName()), "main");
        EntityModelLayerRegistry.registerModelLayer(loc, modelEntry::getLayerDefinition);
        return () -> loc;
    }

    @Override
    public <T extends Entity> Supplier<EntityRendererProvider<T>> register(String modid, EntityRendererEntryType<T> rendererEntry) {
        EntityRendererRegistry.register(rendererEntry.getEntityType(), rendererEntry.getRendererProvider());
        return rendererEntry::getRendererProvider;
    }
}
