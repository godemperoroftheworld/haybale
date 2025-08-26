package com.t2pellet.haybale.registry;

import com.t2pellet.haybale.registry.api.EntityEntryType;
import com.t2pellet.haybale.registry.api.RegistryClass;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Cow;

@RegistryClass.IRegistryClass(EntityType.class)
public class HaybaleEntities implements RegistryClass {

    @IRegistryEntry
    public static final EntityEntryType<Cow> TEST_ENTITY = new EntityEntryType<>("cow", Cow::new, Cow::createAttributes, MobCategory.CREATURE, 1.5F, 0.5F);
}
