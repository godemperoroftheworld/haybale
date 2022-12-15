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
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Function;

public class FabricClientRegistry implements IClientRegistry {

    @Override
    public <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> aNew) {
        ParticleFactoryRegistry.getInstance().register(type, aNew::apply);
    }

    @Override
    public <T extends Entity> void registerEntityRenderer(String modid, String name, EntityType<T> type, EntityRendererProvider<T> renderSupplier, ModelLayerLocation modelLayerLocation, LayerDefinition modelData) {
        registerRenderer(type, renderSupplier);
        EntityModelLayerRegistry.registerModelLayer(modelLayerLocation, () -> modelData);
    }

    private <T extends Entity> void registerRenderer(EntityType<? extends T> type, EntityRendererProvider<T> supplier) {
        EntityRendererRegistry.register(type, supplier);
    }


}
