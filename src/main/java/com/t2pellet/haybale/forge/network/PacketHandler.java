//? if forge {
/*package com.t2pellet.haybale.forge.network;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.services.IPacketHandler;
import com.t2pellet.haybale.common.network.api.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler implements IPacketHandler {

    private final String PROTOCOL_VERSION = "4";
    private final Map<ResourceLocation, Integer> idMap = new HashMap<>();
    private final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Services.VERSION_HELPER.getResourceLocation(Haybale.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass) {
        idMap.put(Services.VERSION_HELPER.getResourceLocation(modid, name), idMap.size());
        registerPacket(modid, name, packetClass);
    }

    public void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass) {
        idMap.put(Services.VERSION_HELPER.getResourceLocation(modid, name), idMap.size());
        registerPacket(modid, name, packetClass);
    }

    private <T extends Packet> void registerPacket(String modid, String name, Class<T> packetClass) {
        ResourceLocation id = Services.VERSION_HELPER.getResourceLocation(modid, name);
        INSTANCE.registerMessage(idMap.get(id), packetClass, Packet::encode, friendlyByteBuf -> {
            try {
                return packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(friendlyByteBuf);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                    InvocationTargetException ex) {
                Haybale.LOG.error("Error: Failed to instantiate packet - " + id);
            }
            return null;
        }, (t, contextSupplier) -> {
            if (contextSupplier.get().getDirection().getReceptionSide().isClient()) {
                Services.SIDE.scheduleClient(t.getExecutor());
            } else {
                Services.SIDE.scheduleServer(t.getExecutor());
            }
            contextSupplier.get().setPacketHandled(true);
        });
    }

    @Override
    public <T extends Packet> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer... players) {
        for (ServerPlayer player : players) {
            sendTo(packet, player);
        }
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