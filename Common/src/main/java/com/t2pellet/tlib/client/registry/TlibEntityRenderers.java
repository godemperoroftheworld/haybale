package com.t2pellet.tlib.client.registry;

import com.t2pellet.tlib.client.registry.api.EntityRendererEntryType;
import com.t2pellet.tlib.registry.TlibEntities;
import com.t2pellet.tlib.registry.api.RegistryClass;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.animal.Cow;

@RegistryClass.IRegistryClass(EntityRendererProvider.class)
public class TlibEntityRenderers implements RegistryClass {

    @RegistryClass.IRegistryEntry
    public static final EntityRendererEntryType<Cow> COW_RENDERER = new EntityRendererEntryType<>(TlibEntities.TEST_ENTITY::get, CowRenderer::new);
}
