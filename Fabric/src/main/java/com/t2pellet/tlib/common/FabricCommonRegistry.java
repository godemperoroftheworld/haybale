package com.t2pellet.tlib.common;

import com.t2pellet.tlib.common.registry.ICommonRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricCommonRegistry implements ICommonRegistry {
    @Override
    public Supplier<SimpleParticleType> registerParticle(String modid, String name) {
        SimpleParticleType type = new SimpleParticleType(true) {};
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(modid, name), type);
        return () -> type;
    }

    @Override
    public <T extends LivingEntity> Supplier<EntityType<T>> registerEntity(String modid, String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, AttributeSupplier.Builder builder) {
        EntityType<T> type = Registry.register(
                Registry.ENTITY_TYPE,
                new ResourceLocation(modid, name),
                EntityType.Builder.of(factory, MobCategory.CREATURE)
                        .clientTrackingRange(48).updateInterval(3).sized(width, height)
                        .build(name));
        FabricDefaultAttributeRegistry.register(type, builder.build());
        return () -> type;
    }

    @Override
    public void registerSound(SoundEvent sound) {
        Registry.register(Registry.SOUND_EVENT, sound.getLocation(), sound);
    }

    @Override
    public Supplier<Item> registerItem(String modid, String name, Item.Properties properties) {
        Item item = Registry.register(Registry.ITEM, new ResourceLocation(modid, name), new Item(properties));
        return () -> item;
    }

    @Override
    public void registerEvents() {
    }
}
