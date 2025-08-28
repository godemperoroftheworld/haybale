package com.t2pellet.haybale.common.capability.api;

import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.network.capability.CapabilityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public abstract class AbstractCapability<E extends Entity & ICapabilityHaver> implements Capability {

    protected E entity;

    protected AbstractCapability(E entity) {
        this.entity = entity;
    }

    public void synchronize() {
        Entity entity = this.entity;
        if (!Services.VERSION_HELPER.getLevel(entity).isClientSide) {
            CapabilityPacket<E> packet = new CapabilityPacket<>(this.entity, getClass());
            Services.PACKET_HANDLER.sendInRange(packet, this.entity, 128);
        }
    }

    public void synchronizeTo(ServerPlayer player) {
        if (!Services.VERSION_HELPER.getLevel(entity).isClientSide) {
            CapabilityPacket<E> packet = new CapabilityPacket<>(entity, getClass());
            Services.PACKET_HANDLER.sendTo(packet, player);
        }
    }

}
