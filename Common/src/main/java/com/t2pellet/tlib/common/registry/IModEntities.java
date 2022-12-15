package com.t2pellet.tlib.common.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

public interface IModEntities {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IEntity {
        String name();
        MobCategory category();
        float width();
        float height();
    }

    class TLibEntity<T extends LivingEntity> {
        public final Supplier<EntityType<T>> TYPE = null;
        public final EntityType.EntityFactory<T> _factory;
        public  final AttributeSupplier.Builder _attributes;

        public TLibEntity(EntityType.EntityFactory<T> factory, AttributeSupplier.Builder attributes) {
            this._factory = factory;
            this._attributes = attributes;
        }
    }

}
