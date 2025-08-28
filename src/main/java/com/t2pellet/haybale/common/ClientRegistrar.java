package com.t2pellet.haybale.common;

import com.t2pellet.haybale.client.registry.IClientRegistry;
import com.t2pellet.haybale.client.registry.api.EntityModelEntryType;
import com.t2pellet.haybale.client.registry.api.EntityRendererEntryType;
import com.t2pellet.haybale.client.registry.api.ParticleFactoryEntryType;
import com.t2pellet.haybale.common.registry.api.EntryType;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleType;

import java.lang.reflect.Field;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public class ClientRegistrar extends BaseRegistrar {

    public static final ClientRegistrar INSTANCE = new ClientRegistrar();
    private static final IClientRegistry CLIENT_REGISTRY = ServiceLoader.load(IClientRegistry.class).findFirst().orElseThrow(
            () -> new NullPointerException("Failed to load client registry service")
    );

    private ClientRegistrar() {}

    @Override
    protected boolean checkIsValid(Class<?> type) {
        return type.isAssignableFrom(ModelLayerLocation.class)
                || type.isAssignableFrom(EntityRendererProvider.class)
                || type.isAssignableFrom(ParticleType.class);
    }

    @Override
    protected void doGenericRegistration(String modid, Class<?> registryType, Field declaredField) throws IllegalAccessException {
        // Get EntryType and register accordingly based on its subclass
        EntryType<?> entryType = (EntryType<?>) declaredField.get(null);
        if (registryType.isAssignableFrom(entryType.type)) {
            Supplier<?> result;
            if (entryType instanceof EntityModelEntryType modelEntryType) {
                result = CLIENT_REGISTRY.register(modid, modelEntryType);
            } else if (entryType instanceof EntityRendererEntryType<?> rendererEntryType) {
                result = CLIENT_REGISTRY.register(modid, rendererEntryType);
            } else if (entryType instanceof ParticleFactoryEntryType particleFactoryEntryType) {
                result = CLIENT_REGISTRY.register(modid, particleFactoryEntryType);
            } else result = null;
            setField("supplier", entryType, result);
        }
    }

}
