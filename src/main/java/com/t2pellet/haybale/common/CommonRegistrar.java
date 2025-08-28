package com.t2pellet.haybale.common;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.client.registry.IClientRegistry;
import com.t2pellet.haybale.common.capability.api.Capability;
import com.t2pellet.haybale.common.capability.api.registry.IModCapabilities;
import com.t2pellet.haybale.common.capability.registry.CapabilityRegistrar;
import com.t2pellet.haybale.common.network.api.registry.IModPackets;
import com.t2pellet.haybale.common.registry.api.*;
import com.t2pellet.haybale.registry.ICommonRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.lang.reflect.Field;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public class CommonRegistrar extends BaseRegistrar {

    public static final CommonRegistrar INSTANCE = new CommonRegistrar();

    private static final ICommonRegistry COMMON_REGISTRY = ServiceLoader.load(ICommonRegistry.class).findFirst().orElseThrow(
            () -> new NullPointerException("Failed to load common registry service")
    );

    private CommonRegistrar() {}

    @Override
    protected boolean checkIsValid(Class<?> type) {
        return type.isAssignableFrom(SimpleParticleType.class)
                || type.isAssignableFrom(EntityType.class)
                || type.isAssignableFrom(SoundEvent.class)
                || type.isAssignableFrom(Item.class);
    }

    @Override
    protected void doGenericRegistration(String modid, Class<?> registryType, Field declaredField) throws IllegalAccessException {
        // Get EntryType and register accordingly based on its subclass
        // This is one of two places where we have to do this if / elif block. The other is in EntryType::isValid
        EntryType<?> entryType = (EntryType<?>) declaredField.get(null);
        if (registryType.isAssignableFrom(entryType.type)) {
            Supplier<?> result;
            if (entryType instanceof ParticleEntryType) result = COMMON_REGISTRY.register(modid, (ParticleEntryType) entryType);
            else if (entryType instanceof EntityEntryType<?>) result = COMMON_REGISTRY.register(modid, (EntityEntryType<? extends LivingEntity>) entryType);
            else if (entryType instanceof SoundEntryType) result = COMMON_REGISTRY.register(modid, (SoundEntryType) entryType);
            else if (entryType instanceof ItemEntryType) result = COMMON_REGISTRY.register(modid, (ItemEntryType) entryType);
            else result = null;
            setField("supplier", entryType, result);
        }
    }


    public void registerPackets(String modid, IModPackets packets) {
        if (packets == null) return;

        Haybale.LOG.debug("Registering packets for modid: " + modid);
        for (Field field : packets.getClass().getDeclaredFields()) {
            IModPackets.IPacket packetInfo = field.getAnnotation(IModPackets.IPacket.class);
            if (packetInfo != null && field.getType().equals(IModPackets.haybalePacket.class)) {
                try {
                    IModPackets.haybalePacket packet = (IModPackets.haybalePacket) field.get(null);
                    if (packetInfo.client()) {
                        COMMON_REGISTRY.registerClientPacket(modid, packetInfo.name(), packet.getPacketClass());
                    } else {
                        COMMON_REGISTRY.registerServerPacket(modid, packetInfo.name(), packet.getPacketClass());
                    }
                } catch (Exception e) {
                    Haybale.LOG.error("Failed to register packets for " + modid);
                    Haybale.LOG.error(e);
                }
            }
        }
    }

    public void registerCapabilities(String modid, IModCapabilities capabilities) {
        if (capabilities == null) return;

        Haybale.LOG.debug("Registering capabilities for modid: " + modid);
        for (Field field : capabilities.getClass().getDeclaredFields()) {
            IModCapabilities.ICapability capabilityInfo = field.getAnnotation(IModCapabilities.ICapability.class);
            if (capabilityInfo != null && field.getType().equals(IModCapabilities.haybaleCapability.class)) {
                try {
                    IModCapabilities.haybaleCapability<?> capability = (IModCapabilities.haybaleCapability<?>) field.get(null);
                    registerCapability(capabilityInfo.value(), capability::get);
                } catch (Exception e) {
                    Haybale.LOG.error("Failed to register capabilities for " + modid);
                    Haybale.LOG.error(e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Capability> void registerCapability(Class<? extends Capability> clazz, CapabilityRegistrar.CapabilityFactory<T> supplier) {
        CapabilityRegistrar.INSTANCE.register((Class<T>) clazz, supplier);
    }
}
