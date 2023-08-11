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
        private ModelLayerLocation model = null;
        public final LayerDefinition _modelData;

        public ModelLayerLocation getModel() {
            return model;
        }

        public TLibEntityModel(LayerDefinition modelData) {
            this._modelData = modelData;
        }
    }
}
