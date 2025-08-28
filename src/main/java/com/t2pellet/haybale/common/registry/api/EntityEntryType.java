package com.t2pellet.haybale.common.registry.api;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public class EntityEntryType<T extends LivingEntity> extends EntryType<EntityType> {

    private final String name;
    private final EntityType.EntityFactory<T> factory;
    private final MobCategory mobCategory;
    private final float width;
    private final float height;
    private final Supplier<AttributeSupplier.Builder> builder;

    public EntityEntryType(String name, EntityType.EntityFactory<T> factory, Supplier<AttributeSupplier.Builder> builder, MobCategory mobCategory, float width, float height) {
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

    public AttributeSupplier buildAttributes() {
        return builder.get().build();
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
