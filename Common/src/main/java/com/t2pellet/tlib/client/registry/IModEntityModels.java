package com.t2pellet.tlib.client.registry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

public interface IModEntityModels {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IEntityModel {
        String value();
    }

    class TLibEntityModel<T extends Entity> {
        public final ModelLayerLocation MODEL = null;
        public final EntityType<T> _type;
        public final EntityRendererProvider<T> _renderProvider;
        public final LayerDefinition _modelData;

        public TLibEntityModel(EntityType<T> type, EntityRendererProvider<T> renderProvider, LayerDefinition modelData) {
            this._type = type;
            this._renderProvider = renderProvider;
            this._modelData = modelData;
        }
    }
}
