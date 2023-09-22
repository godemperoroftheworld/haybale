package com.t2pellet.tlib;

import com.t2pellet.tlib.client.registry.api.EntityModelEntryType;
import com.t2pellet.tlib.client.registry.api.EntityRendererEntryType;
import com.t2pellet.tlib.client.registry.api.ParticleFactoryEntryType;
import com.t2pellet.tlib.registry.api.EntryType;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleType;

import java.lang.reflect.Field;

class ClientRegistrar extends BaseRegistrar {

    public static final ClientRegistrar INSTANCE = new ClientRegistrar();

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
            Object result;
            if (entryType instanceof EntityModelEntryType modelEntryType) {
                result = Services.CLIENT_REGISTRY.register(modid, modelEntryType);
            } else if (entryType instanceof EntityRendererEntryType<?> rendererEntryType) {
                result = Services.CLIENT_REGISTRY.register(modid, rendererEntryType);
            } else if (entryType instanceof ParticleFactoryEntryType<?> particleFactoryEntryType) {
                result = Services.CLIENT_REGISTRY.register(modid, particleFactoryEntryType);
            } else result = null;
            setField("value", entryType, result);
        }
    }

}
