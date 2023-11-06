package com.t2pellet.tlib.client.registry.api;

import com.t2pellet.tlib.registry.api.EntryType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class EntityRendererEntryType<T extends Entity> extends EntryType<EntityRendererProvider> {

    private final Supplier<EntityType<T>> entityType;
    private final EntityRendererProvider<T> rendererProvider;

    public EntityRendererEntryType(Supplier<EntityType<T>> entityType, EntityRendererProvider<T> rendererProvider) {
        super(EntityRendererProvider.class);
        this.entityType = entityType;
        this.rendererProvider = rendererProvider;
    }

    public EntityType<T> getEntityType() {
        return entityType.get();
    }

    public EntityRendererProvider<T> getRendererProvider() {
        return rendererProvider;
    }
}
