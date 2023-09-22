package com.t2pellet.tlib.client;

import com.t2pellet.tlib.client.registry.IClientRegistry;
import com.t2pellet.tlib.client.registry.api.EntityModelEntryType;
import com.t2pellet.tlib.client.registry.api.EntityRendererEntryType;
import com.t2pellet.tlib.client.registry.api.ParticleFactoryEntryType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeClientRegistry implements IClientRegistry {

    @Override
    @SuppressWarnings("unchecked")
    public Supplier<ParticleType<SimpleParticleType>> register(String modid, ParticleFactoryEntryType particleFactoryEntry) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<RegisterParticleProvidersEvent>) particleFactoryRegisterEvent -> {
            particleFactoryRegisterEvent.register(particleFactoryEntry.get(), spriteSet -> particleFactoryEntry.getProviderFunction().apply(spriteSet));
        });
        return particleFactoryEntry::get;
    }

    @Override
    public Supplier<ModelLayerLocation> register(String modid, EntityModelEntryType modelEntry) {
        Lazy<ModelLayerLocation> locSupplier = () -> new ModelLayerLocation(new ResourceLocation(modid, modelEntry.getName()), "main");
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
            event.registerLayerDefinition(locSupplier.get(), modelEntry::getLayerDefinition);
        });
        return locSupplier;
    }

    @Override
    public <T extends Entity> Supplier<EntityRendererProvider<T>> register(String modid, EntityRendererEntryType<T> rendererEntry) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerEntityRenderer(rendererEntry.getEntityType(), rendererEntry.getRendererProvider());
        });
        return rendererEntry::getRendererProvider;
    }
}
