package com.t2pellet.haybale.client.registry;

import com.t2pellet.haybale.client.registry.api.EntityRendererEntryType;
import com.t2pellet.haybale.common.registry.HaybaleEntities;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.Cow;

@RegistryClass.IRegistryClass(EntityRendererProvider.class)
public class HaybaleEntityRenderers implements RegistryClass {

    @IRegistryEntry
    public static final EntityRendererEntryType<Cow> COW_RENDERER = new EntityRendererEntryType<>(HaybaleEntities.TEST_ENTITY::get, CowRenderer::new);
}
