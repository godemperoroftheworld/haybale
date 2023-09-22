package com.t2pellet.tlib.registry;

import com.t2pellet.tlib.network.api.Packet;
import com.t2pellet.tlib.registry.api.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public interface ICommonRegistry {

    Supplier<SimpleParticleType> register(String modid, ParticleEntryType particleEntryType);
    <T extends LivingEntity> Supplier<EntityType<T>> register(String modid, EntityEntryType<T> entityEntryType);
    Supplier<SoundEvent> register(String modid, SoundEntryType soundEntryType);
    Supplier<Item> register(String modid, ItemEntryType itemEntryType);
    void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass);
    void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass);

}
