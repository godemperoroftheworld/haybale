//? if fabric {
/*package com.t2pellet.haybale.fabric.registry;

import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.network.api.Packet;
import com.t2pellet.haybale.registry.ICommonRegistry;
import com.t2pellet.haybale.common.registry.api.EntityEntryType;
import com.t2pellet.haybale.common.registry.api.ItemEntryType;
import com.t2pellet.haybale.common.registry.api.ParticleEntryType;
import com.t2pellet.haybale.common.registry.api.SoundEntryType;
import com.t2pellet.haybale.fabric.network.PacketHandler;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
//? if >= 1.19.4 {
import net.minecraft.core.registries.BuiltInRegistries;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class CommonRegistry implements ICommonRegistry {


    @Override
    public Supplier<SimpleParticleType> register(String modid, ParticleEntryType particleEntryType) {
        ResourceLocation id = new ResourceLocation(modid, particleEntryType.getName());
        SimpleParticleType type = Registry.register(
                //? if >= 1.19.4 {
                BuiltInRegistries.PARTICLE_TYPE,
                //?} else
                /^Registry.PARTICLE_TYPE,^/
                id,
                FabricParticleTypes.simple()
        );
        return () -> type;
    }

    @Override
    public <T extends LivingEntity> Supplier<EntityType<T>> register(String modid, EntityEntryType<T> entityEntryType) {
        EntityType<T> type = Registry.register(
                //? if >= 1.19.4 {
                BuiltInRegistries.ENTITY_TYPE,
                //?} else
                /^Registry.ENTITY_TYPE,^/
                new ResourceLocation(modid, entityEntryType.getName()),
                EntityType.Builder.of(entityEntryType.getFactory(), MobCategory.CREATURE)
                        .clientTrackingRange(48).updateInterval(3).sized(entityEntryType.getWidth(), entityEntryType.getHeight())
                        .build(entityEntryType.getName()));
        FabricDefaultAttributeRegistry.register(
                type,
                //? if >= 1.19.4 {
                entityEntryType.buildAttributes().build()
                //?} else
                /^entityEntryType.buildAttributes()^/
        );
        return () -> type;
    }

    @Override
    public Supplier<SoundEvent> register(String modid, SoundEntryType soundEntryType) {
        ResourceLocation location = new ResourceLocation(modid, soundEntryType.getName());
        //? if >= 1.19.4 {
        SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(location);
        //?} else
        /^SoundEvent soundEvent = new SoundEvent(location);^/
        Registry.register(
                //? if >= 1.19.4 {
                BuiltInRegistries.SOUND_EVENT,
                //?} else
                /^Registry.SOUND_EVENT,^/
                location,
                soundEvent
        );
        return () -> soundEvent;
    }

    @Override
    public Supplier<Item> register(String modid, ItemEntryType itemEntryType) {
        Item item = Registry.register(
                //? if >= 1.19.4 {
                BuiltInRegistries.ITEM,
                //?} else
                /^Registry.ITEM,^/
                new ResourceLocation(modid, itemEntryType.getName()), new Item(itemEntryType.getProperties())
        );
        return () -> item;
    }

    @Override
    public void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass) {
        PacketHandler packetHandler = (PacketHandler) Services.PACKET_HANDLER;
        packetHandler.registerServerPacket(modid, name, packetClass);
    }

    @Override
    public void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass) {
        PacketHandler packetHandler = (PacketHandler) Services.PACKET_HANDLER;
        packetHandler.registerClientPacket(modid, name, packetClass);
    }
}
*///?}