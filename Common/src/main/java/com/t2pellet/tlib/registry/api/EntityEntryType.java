package com.t2pellet.tlib.registry.api;

import com.google.common.reflect.TypeToken;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class EntityEntryType<T extends LivingEntity> extends EntryType<EntityType> {

    private final String name;
    private final EntityType.EntityFactory<T> factory;
    private final MobCategory mobCategory;
    private final float width;
    private final float height;
    private final AttributeSupplier.Builder builder;

    public EntityEntryType(String name, EntityType.EntityFactory<T> factory, AttributeSupplier.Builder builder, MobCategory mobCategory, float width, float height) {
        super(EntityType.class);
        this.name = name;
        this.factory = factory;
        this.builder = builder;
        this.mobCategory = mobCategory;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntityType<T> get() {
        return super.get();
    }

    public EntityType.EntityFactory<T> getFactory() {
        return factory;
    }

    public AttributeSupplier.Builder getBuilder() {
        return builder;
    }

    public MobCategory getMobCategory() {
        return mobCategory;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
