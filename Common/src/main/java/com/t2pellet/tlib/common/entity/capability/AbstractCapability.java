package com.t2pellet.tlib.common.entity.capability;

import com.t2pellet.tlib.Services;
import com.t2pellet.tlib.common.network.capability.CapabilityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public abstract class AbstractCapability<E extends Entity & ICapabilityHaver> implements Capability {

    protected E entity;

    protected AbstractCapability(E entity) {
        this.entity = entity;
    }

    public void synchronize() {
        Entity entity = this.entity;
        if (!entity.level.isClientSide) {
            CapabilityPacket<E> packet = new CapabilityPacket<>(this.entity, getClass());
            Services.PACKET_HANDLER.sendInRange(packet, this.entity, 128);
        }
    }

    public void synchronizeTo(ServerPlayer player) {
        if (!entity.level.isClientSide) {
            CapabilityPacket<E> packet = new CapabilityPacket<>(entity, getClass());
            Services.PACKET_HANDLER.sendTo(packet, player);
        }
    }

}
