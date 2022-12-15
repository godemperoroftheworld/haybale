package com.t2pellet.tlib.common.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public interface IPacketHandler {

    <T extends Packet<T>> void registerServerPacket(String modid, String name, Class<T> packetClass);
    <T extends Packet<T>> void registerClientPacket(String modid, String name, Class<T> packetClass);

    <T extends Packet<T>> void sendToServer(Packet<T> packet);
    <T extends Packet<T>> void sendTo(Packet<T> packet, ServerPlayer player);
    <T extends Packet<T>> void sendTo(Packet<T> packet, ServerPlayer ...players);
    <T extends Packet<T>> void sendInRange(Packet<T> packet, Entity e, float range);
    <T extends Packet<T>> void sendInArea(Packet<T> packet, Level world, AABB area);

}
