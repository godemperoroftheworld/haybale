package com.t2pellet.tlib;

import com.t2pellet.tlib.client.registry.IModEntityModels;
import com.t2pellet.tlib.client.registry.IModEntityRenderers;
import com.t2pellet.tlib.client.registry.IModParticleFactories;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class ClientRegistrar {

    public static void register(String modid, IModEntityModels models) {
        if (models == null) return;

        TenzinLib.LOG.debug("Registering entity models for mod: " + modid);
        for (Field field : models.getClass().getDeclaredFields()) {
            IModEntityModels.IEntityModel modelInfo = field.getDeclaredAnnotation(IModEntityModels.IEntityModel.class);
            if (modelInfo != null && field.getType().equals(IModEntityModels.TLibEntityModel.class)) {
                try {
                    IModEntityModels.TLibEntityModel<? extends Entity> model = (IModEntityModels.TLibEntityModel<? extends Entity>) field.get(null);
                    ModelLayerLocation loc = new ModelLayerLocation(new ResourceLocation(modid, modelInfo.value()), "main");
                    Services.CLIENT_REGISTRY.registerModelLayer(loc, model._modelData);
                    Field result = model.getClass().getDeclaredField("model");
                    setField(result, model, loc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register(String modid, IModEntityRenderers renderers) {
        if (renderers == null) return;

        TenzinLib.LOG.debug("Registering entity renderers for mod: " + modid);
        for (Field field : renderers.getClass().getDeclaredFields()) {
            if (field.getType().equals(IModEntityRenderers.TLibEntityRenderer.class)) {
                try {
                    IModEntityRenderers.TLibEntityRenderer<? extends Entity> renderer = (IModEntityRenderers.TLibEntityRenderer<? extends Entity>) field.get(null);
                    Services.CLIENT_REGISTRY.registerEntityRenderer(fixType(renderer._type), renderer._renderProvider);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Entity> EntityType<T> fixType(EntityType<? extends Entity> type) {
        return (EntityType<T>) type;
    }

    public static void register(String modid, IModParticleFactories particleFactories) {
        if (particleFactories == null) return;

        TenzinLib.LOG.debug("Registering particle factories for mod: " + modid);
        for (Field field : particleFactories.getClass().getDeclaredFields()) {
            IModParticleFactories.IParticleFactory particleFactoryInfo = field.getDeclaredAnnotation(IModParticleFactories.IParticleFactory.class);
            if (particleFactoryInfo != null && field.getType().equals(IModParticleFactories.TLibParticleFactory.class)) {
                try {
                    IModParticleFactories.TLibParticleFactory<? extends ParticleOptions> particleFactory = (IModParticleFactories.TLibParticleFactory<? extends ParticleOptions>) field.get(null);
                    registerParticleFactory(particleFactory);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T extends ParticleOptions> void registerParticleFactory(IModParticleFactories.TLibParticleFactory<T> particleFactory) {
        Services.CLIENT_REGISTRY.registerParticleProvider(particleFactory._particle, particleFactory._provider);
    }

    private static void setField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
