package com.t2pellet.tlib.client.registry;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface IModEntityRenderers {

    class TLibEntityRenderer<T extends Entity> {
        public final EntityType<T> _type;
        public final EntityRendererProvider<T> _renderProvider;

        public TLibEntityRenderer(EntityType<T> type, EntityRendererProvider<T> provider) {
            this._type = type;
            this._renderProvider = provider;
        }

    }
}
