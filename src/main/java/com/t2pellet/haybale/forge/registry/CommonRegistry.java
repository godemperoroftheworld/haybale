//? if forge {
/*package com.t2pellet.haybale.forge.registry;

import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.network.api.Packet;
import com.t2pellet.haybale.registry.ICommonRegistry;
import com.t2pellet.haybale.common.registry.api.EntityEntryType;
import com.t2pellet.haybale.common.registry.api.ItemEntryType;
import com.t2pellet.haybale.common.registry.api.ParticleEntryType;
import com.t2pellet.haybale.common.registry.api.SoundEntryType;
import com.t2pellet.haybale.forge.HaybaleForge;
import com.t2pellet.haybale.forge.HaybaleForgeMod;
import com.t2pellet.haybale.forge.network.PacketHandler;
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

public class CommonRegistry implements ICommonRegistry {

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
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityAttributeCreationEvent>) event -> event.put(result.get(), entityEntryType.buildAttributes().build()));
        return result;
    }

    @Override
    public Supplier<SoundEvent> register(String modid, SoundEntryType soundEntryType) {
        ResourceLocation location = new ResourceLocation(modid, soundEntryType.getName());
        HaybaleForgeMod forgeMod = HaybaleForge.getInstance().get(modid);
        return forgeMod.SOUNDS.register(soundEntryType.getName(), () -> {
            //? if >= 1.19.4 {
            return SoundEvent.createVariableRangeEvent(location);
            //?} else
            /^return new SoundEvent(location);^/
        });
    }

    @Override
    public Supplier<Item> register(String modid, ItemEntryType itemEntryType) {
        HaybaleForgeMod forgeMod = HaybaleForge.getInstance().get(modid);
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