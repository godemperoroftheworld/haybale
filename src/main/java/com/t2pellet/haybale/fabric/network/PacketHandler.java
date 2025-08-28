//? if fabric {
/*package com.t2pellet.haybale.fabric.network;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.services.IPacketHandler;
import com.t2pellet.haybale.common.network.api.Packet;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler implements IPacketHandler {

    private final Map<Class<? extends Packet>, ResourceLocation> idMap;

    public PacketHandler() {
        idMap = new HashMap<>();
    }

    public <T extends Packet> void registerServerPacket(String modid, String name, Class<T> packetClass) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        idMap.put(packetClass, loc);
        ServerPlayNetworking.registerGlobalReceiver(loc, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            try {
                T packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(packetByteBuf);
                Services.SIDE.scheduleServer(packet.getExecutor());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                    InvocationTargetException ex) {
                Haybale.LOG.error("Error: Failed to instantiate packet - " + loc);
            }
        });

    }

    public <T extends Packet> void registerClientPacket(String modid, String name, Class<T> packetClass) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        idMap.put(packetClass, loc);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            _registerClientPacket(loc, packetClass);
        }
    }

    @Environment(EnvType.CLIENT)
    private <T extends Packet> void _registerClientPacket(ResourceLocation id, Class<T> packetClass) {
        ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, responseSender) -> {
            try {
                T packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
                Services.SIDE.scheduleClient(packet.getExecutor());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                    InvocationTargetException ex) {
                Haybale.LOG.error("Error: Failed to instantiate packet - " + id);
            }
        });
    }

    @Override
    public <T extends Packet> void sendToServer(T packet) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(data);
        ClientPlayNetworking.send(idMap.get(packet.getClass()), data);
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer player) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(data);
        ServerPlayNetworking.send(player, idMap.get(packet.getClass()), data);
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer... players) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        ResourceLocation id = idMap.get(packet.getClass());
        packet.encode(data);
        for (ServerPlayer player : players) ServerPlayNetworking.send(player, id, data);
    }

    @Override
    public <T extends Packet> void sendInRange(T packet, Entity e, float range) {
        AABB box = new AABB(e.blockPosition()).inflate(range);
        Level level = Services.VERSION_HELPER.getLevel(e);
        sendInArea(packet, level, box);
    }

    @Override
    public <T extends Packet> void sendInArea(T packet, Level world, AABB area) {
        ServerPlayer[] players = ((ServerLevel) world).players().stream().filter((p) -> area.contains(p.position())).toArray(ServerPlayer[]::new);
        sendTo(packet, players);
    }

}
*///?}