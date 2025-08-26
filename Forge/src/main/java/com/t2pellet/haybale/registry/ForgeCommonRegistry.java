package com.t2pellet.haybale.registry;

import com.t2pellet.haybale.HaybaleForge;
import com.t2pellet.haybale.HaybaleForgeMod;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.network.ForgePacketHandler;
import com.t2pellet.haybale.network.api.Packet;
import com.t2pellet.haybale.registry.api.EntityEntryType;
import com.t2pellet.haybale.registry.api.ItemEntryType;
import com.t2pellet.haybale.registry.api.ParticleEntryType;
import com.t2pellet.haybale.registry.api.SoundEntryType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgeCommonRegistry implements ICommonRegistry {

    @Override
    public Supplier<SimpleParticleType> register(String modid, ParticleEntryType particleEntryType) {
        HaybaleForgeMod forgeMod = HaybaleForge.getInstance().get(modid);
        return forgeMod.PARTICLES.register(particleEntryType.getName(), () -> new SimpleParticleType(true));
    }

    @Override
    public <T extends LivingEntity> Supplier<EntityType<T>> register(String modid, EntityEntryType<T> entityEntryType) {
        HaybaleForgeMod forgeMod = HaybaleForge.getInstance().get(modid);
        RegistryObject<EntityType<T>> result = forgeMod.ENTITIES.register(entityEntryType.getName(), () -> EntityType.Builder.of(entityEntryType.getFactory(), entityEntryType.getMobCategory())
                .clientTrackingRange(48).updateInterval(3).sized(entityEntryType.getWidth(), entityEntryType.getHeight())
                .build(entityEntryType.getName()));
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityAttributeCreationEvent>) event -> event.put(result.get(), entityEntryType.buildAttributes()));
        return result;
    }

    @Override
    public Supplier<SoundEvent> register(String modid, SoundEntryType soundEntryType) {
        ResourceLocation location = new ResourceLocation(modid, soundEntryType.getName());
        HaybaleForgeMod forgeMod = HaybaleForge.getInstance().get(modid);
        return forgeMod.SOUNDS.register(soundEntryType.getName(), () -> new SoundEvent(location));
    }

    @Override
    public Supplier<Item> register(String modid, ItemEntryType itemEntryType) {
        HaybaleForgeMod forgeMod = HaybaleForge.getInstance().get(modid);
        return forgeMod.ITEMS.register(itemEntryType.getName(), () -> new Item(itemEntryType.getProperties()));
    }

    @Override
    public void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass) {
        ForgePacketHandler packetHandler = (ForgePacketHandler) Services.PACKET_HANDLER;
        packetHandler.registerServerPacket(modid, name, packetClass);
    }

    @Override
    public void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass) {
        ForgePacketHandler packetHandler = (ForgePacketHandler) Services.PACKET_HANDLER;
        packetHandler.registerClientPacket(modid, name, packetClass);
    }
}
