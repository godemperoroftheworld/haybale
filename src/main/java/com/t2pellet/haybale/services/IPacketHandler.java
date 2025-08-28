package com.t2pellet.haybale.services;

import com.t2pellet.haybale.common.network.api.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public interface IPacketHandler {
    <T extends Packet> void sendToServer(T packet);
    <T extends Packet> void sendTo(T packet, ServerPlayer player);
    <T extends Packet> void sendTo(T packet, ServerPlayer ...players);
    <T extends Packet> void sendInRange(T packet, Entity e, float range);
    <T extends Packet> void sendInArea(T packet, Level world, AABB area);
}
