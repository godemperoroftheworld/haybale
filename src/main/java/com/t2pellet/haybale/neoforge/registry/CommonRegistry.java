//? if neoforge {
/*package com.t2pellet.haybale.neoforge.registry;

import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.network.api.Packet;
import com.t2pellet.haybale.registry.ICommonRegistry;
import com.t2pellet.haybale.common.registry.api.EntityEntryType;
import com.t2pellet.haybale.common.registry.api.ItemEntryType;
import com.t2pellet.haybale.common.registry.api.ParticleEntryType;
import com.t2pellet.haybale.common.registry.api.SoundEntryType;
import com.t2pellet.haybale.neoforge.HaybaleNeoforge;
import com.t2pellet.haybale.neoforge.HaybaleNeoforgeMod;
import com.t2pellet.haybale.neoforge.network.PacketHandler;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommonRegistry implements ICommonRegistry {

    @Override
    public Supplier<SimpleParticleType> register(String modid, ParticleEntryType particleEntryType) {
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        return forgeMod.PARTICLES.register(particleEntryType.getName(), () -> new SimpleParticleType(true));
    }

    @Override
    public <T extends LivingEntity> Supplier<EntityType<T>> register(String modid, EntityEntryType<T> entityEntryType) {
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        //? if >= 1.21.2 {
        ResourceLocation id = Services.VERSION_HELPER.getResourceLocation(modid, entityEntryType.getName());
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, id);
        //?}
        DeferredHolder<EntityType<?>, EntityType<T>> result = forgeMod.ENTITIES.register(entityEntryType.getName(), () -> EntityType.Builder.of(entityEntryType.getFactory(), entityEntryType.getMobCategory())
                .clientTrackingRange(48).updateInterval(3).sized(entityEntryType.getWidth(), entityEntryType.getHeight())
                //? if < 1.21.2 {
                /^.build(entityEntryType.getName()));
                ^///?} else
                .build(key));
        forgeMod.modBus.addListener((Consumer<EntityAttributeCreationEvent>) event -> event.put(result.get(), entityEntryType.buildAttributes().build()));
        return result;
    }

    @Override
    public Supplier<SoundEvent> register(String modid, SoundEntryType soundEntryType) {
        ResourceLocation location = Services.VERSION_HELPER.getResourceLocation(modid, soundEntryType.getName());
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        return forgeMod.SOUNDS.register(soundEntryType.getName(), () -> SoundEvent.createVariableRangeEvent(location));
    }

    @Override
    public Supplier<Item> register(String modid, ItemEntryType itemEntryType) {
        HaybaleNeoforgeMod forgeMod = HaybaleNeoforge.getInstance().get(modid);
        return forgeMod.ITEMS.register(itemEntryType.getName(), () -> new Item(itemEntryType.getProperties()));
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