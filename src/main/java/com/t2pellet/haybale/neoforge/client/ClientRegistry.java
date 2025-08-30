//? if neoforge {
package com.t2pellet.haybale.neoforge.client;

import com.t2pellet.haybale.client.registry.IClientRegistry;
import com.t2pellet.haybale.client.registry.api.EntityModelEntryType;
import com.t2pellet.haybale.client.registry.api.EntityRendererEntryType;
import com.t2pellet.haybale.client.registry.api.ParticleFactoryEntryType;
import com.t2pellet.haybale.neoforge.HaybaleNeoforge;
import com.t2pellet.haybale.neoforge.HaybaleNeoforgeMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientRegistry implements IClientRegistry {

    @Override
    @SuppressWarnings("unchecked")
    public Supplier<ParticleType<SimpleParticleType>> register(String modid, ParticleFactoryEntryType particleFactoryEntry) {
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        forgeMod.modBus.addListener((Consumer<RegisterParticleProvidersEvent>) particleFactoryRegisterEvent -> {
            particleFactoryRegisterEvent.registerSpriteSet(particleFactoryEntry.get(), spriteSet -> particleFactoryEntry.getProviderFunction().apply(spriteSet));
        });
        return particleFactoryEntry::get;
    }

    @Override
    public Supplier<ModelLayerLocation> register(String modid, EntityModelEntryType modelEntry) {
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        Lazy<ModelLayerLocation> locSupplier = () -> new ModelLayerLocation(new ResourceLocation(modid, modelEntry.getName()), "main");
        forgeMod.modBus.addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
            event.registerLayerDefinition(locSupplier.get(), modelEntry::getLayerDefinition);
        });
        return locSupplier;
    }

    @Override
    public <T extends Entity> Supplier<EntityRendererProvider<T>> register(String modid, EntityRendererEntryType<T> rendererEntry) {
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        forgeMod.modBus.addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerEntityRenderer(rendererEntry.getEntityType(), rendererEntry.getRendererProvider());
        });
        return rendererEntry::getRendererProvider;
    }
}
//?}