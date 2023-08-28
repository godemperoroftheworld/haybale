package com.t2pellet.tlib.common.registry;

import com.t2pellet.tlib.common.network.Packet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface ICommonRegistry {

    Supplier<SimpleParticleType> registerParticle(String modid, String name);

    <T extends LivingEntity> Supplier<EntityType<T>> registerEntity(String modid, String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, AttributeSupplier.Builder builder);

    void registerSound(SoundEvent event);

    Supplier<Item> registerItem(String modid, String name, Item.Properties properties);

    void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass);
    void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass);

}
