package com.t2pellet.tlib.client;

import com.t2pellet.tlib.client.registry.IClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeClientRegistry implements IClientRegistry {

    @Override
    public <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> aNew) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<ParticleFactoryRegisterEvent>) particleFactoryRegisterEvent -> {
            Minecraft.getInstance().particleEngine.register(type, aNew::apply);
        });
    }

    @Override
    public <T extends Entity> ModelLayerLocation registerEntityRenderer(String modid, String name, EntityType<T> type, EntityRendererProvider<T> renderSupplier, Supplier<ModelLayerLocation> model, LayerDefinition modelData) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityRenderersEvent.RegisterRenderers>) event -> {
            event.registerEntityRenderer(type, renderSupplier);
        });
        ModelLayerLocation location = model.get();
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityRenderersEvent.RegisterLayerDefinitions>) event -> {
            event.registerLayerDefinition(location, () -> modelData);
        });
        return location;
    }
}
