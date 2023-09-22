package com.t2pellet.tlib.registry;

import com.t2pellet.tlib.Services;
import com.t2pellet.tlib.network.FabricPacketHandler;
import com.t2pellet.tlib.network.api.Packet;
import com.t2pellet.tlib.registry.api.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FabricCommonRegistry implements ICommonRegistry {


    @Override
    public Supplier<SimpleParticleType> register(String modid, ParticleEntryType particleEntryType) {
        SimpleParticleType type = new SimpleParticleType(true) {};
        Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(modid, particleEntryType.getName()), type);
        return () -> type;
    }

    @Override
    public <T extends LivingEntity> Supplier<EntityType<T>> register(String modid, EntityEntryType<T> entityEntryType) {
        EntityType<T> type = Registry.register(
                Registry.ENTITY_TYPE,
                new ResourceLocation(modid, entityEntryType.getName()),
                EntityType.Builder.of(entityEntryType.getFactory(), MobCategory.CREATURE)
                        .clientTrackingRange(48).updateInterval(3).sized(entityEntryType.getWidth(), entityEntryType.getHeight())
                        .build(entityEntryType.getName()));
        FabricDefaultAttributeRegistry.register(type, entityEntryType.getBuilder().build());
        return () -> type;
    }

    @Override
    public Supplier<SoundEvent> register(String modid, SoundEntryType soundEntryType) {
        ResourceLocation location = new ResourceLocation(modid, soundEntryType.getName());
        SoundEvent soundEvent = new SoundEvent(location);
        Registry.register(Registry.SOUND_EVENT, location, soundEvent);
        return () -> soundEvent;
    }

    @Override
    public Supplier<Item> register(String modid, ItemEntryType itemEntryType) {
        Item item = Registry.register(Registry.ITEM, new ResourceLocation(modid, itemEntryType.getName()), new Item(itemEntryType.getProperties()));
        return () -> item;
    }

    @Override
    public void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass) {
        FabricPacketHandler packetHandler = (FabricPacketHandler) Services.PACKET_HANDLER;
        packetHandler.registerServerPacket(modid, name, packetClass);
    }

    @Override
    public void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass) {
        FabricPacketHandler packetHandler = (FabricPacketHandler) Services.PACKET_HANDLER;
        packetHandler.registerClientPacket(modid, name, packetClass);
    }
}
