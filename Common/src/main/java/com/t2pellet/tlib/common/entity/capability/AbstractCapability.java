package com.t2pellet.tlib.common.entity.capability;

import com.t2pellet.tlib.Services;
import com.t2pellet.tlib.common.network.capability.CapabilityPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;

public abstract class AbstractCapability<E extends ICapabilityHaver & EntityAccess> implements Capability {

    protected E e;

    protected AbstractCapability(E e) {
        this.e = e;
    }

    public void synchronize() {
        Entity entity = (Entity) e;
        if (!entity.level.isClientSide) {
            CapabilityPacket<E> packet = new CapabilityPacket<>(e, getClass());
            Services.PACKET_HANDLER.sendInRange(packet, (Entity) e, 128);
        }
    }

    public void synchronizeTo(ServerPlayer player) {
        Entity entity = (Entity) e;
        if (!entity.level.isClientSide) {
            CapabilityPacket<E> packet = new CapabilityPacket<>(e, getClass());
            Services.PACKET_HANDLER.sendTo(packet, player);
        }
    }

}
