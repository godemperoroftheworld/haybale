package com.t2pellet.tlib.client;

import com.t2pellet.tlib.client.registry.IClientRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;
import java.util.function.Supplier;

public class FabricClientRegistry implements IClientRegistry {
    @Override
    public <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> aNew) {
        ParticleFactoryRegistry.getInstance().register(type, aNew::apply);
    }

    @Override
    public ModelLayerLocation registerEntityRenderer(String modid, String name, EntityRendererProvider<? extends Entity> renderSupplier, Supplier<ModelLayerLocation> model, LayerDefinition modelData) {
        registerRenderer(getType(modid, name), sanitizeSupplier(renderSupplier));
        ModelLayerLocation location = model.get();
        EntityModelLayerRegistry.registerModelLayer(location, () -> modelData);
        return location;
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> EntityType<? extends T> getType(String modid, String name) {
        return (EntityType<? extends T>) Registry.ENTITY_TYPE.get(new ResourceLocation(modid, name));
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity> EntityRendererProvider<T> sanitizeSupplier(EntityRendererProvider<? extends Entity> supplier) {
        return (EntityRendererProvider<T>) supplier;
    }

    private <T extends Entity> void registerRenderer(EntityType<? extends T> type, EntityRendererProvider<T> supplier) {
        EntityRendererRegistry.register(type, supplier);
    }


}
