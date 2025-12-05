//? if fabric {
/*package com.t2pellet.haybale.fabric.network;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.utils.VersionHelper;
import com.t2pellet.haybale.services.IPacketHandler;
import com.t2pellet.haybale.common.network.api.Packet;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
//? if >= 1.20.5 {
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
//?}
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
//? if >= 1.20.5 {
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler implements IPacketHandler {

    private final Map<Class<? extends Packet>, ResourceLocation> idMap;

    //? if >= 1.20.5 {
    private static <T extends Packet> StreamCodec<FriendlyByteBuf, FabricPacket<T>> getCodec(Class<T> packetClass, ResourceLocation id) {
        return StreamCodec.of(
                (packetByteBuf, fabricPacket) -> {
                    // Encode
                    fabricPacket.encode(packetByteBuf);
                },
                (packetByteBuf) -> {
                    // Decode
                    try {
                        T packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(packetByteBuf);
                        return new FabricPacket<>(packet, id);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                            InvocationTargetException ex) {
                        Haybale.LOG.error("Error: Failed to instantiate packet - " + id);
                    }
                    return null;
                }
        );
    }

    private static class FabricPacket<T extends Packet> implements CustomPacketPayload {

        private final T packet;
        private final Type<FabricPacket<T>> type;

        public FabricPacket(T packet, ResourceLocation id) {
            this.packet = packet;
            this.type = new CustomPacketPayload.Type<>(id);
        }

        @Override
        public @NotNull Type<FabricPacket<T>> type() {
            return this.type;
        }

        public void encode(FriendlyByteBuf packetByteBuf) {
            this.packet.encode(packetByteBuf);
        }

        public Runnable executor() {
            return this.packet.getExecutor();
        }
    }
    //?}

    public PacketHandler() {
        idMap = new HashMap<>();
    }

    public <T extends Packet> void registerServerPacket(String modid, String name, Class<T> packetClass) {
        ResourceLocation loc = VersionHelper.getResourceLocation(modid, name);
        idMap.put(packetClass, loc);
        //? if < 1.20.5 {
        /^ServerPlayNetworking.registerGlobalReceiver(loc, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            try {
                T packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(packetByteBuf);
                Services.SIDE.scheduleServer(packet.getExecutor());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                    InvocationTargetException ex) {
                Haybale.LOG.error("Error: Failed to instantiate packet - " + loc);
            }
        });
        ^///?} else {
        CustomPacketPayload.Type<FabricPacket<T>> type = new CustomPacketPayload.Type<>(loc);
        PayloadTypeRegistry.playC2S().register(type, PacketHandler.getCodec(packetClass, loc));
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            Services.SIDE.scheduleServer(payload.executor());
        });
        //?}
    }

    public <T extends Packet> void registerClientPacket(String modid, String name, Class<T> packetClass) {
        ResourceLocation loc = VersionHelper.getResourceLocation(modid, name);
        idMap.put(packetClass, loc);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            _registerClientPacket(loc, packetClass);
        }
    }

    @Environment(EnvType.CLIENT)
    private <T extends Packet> void _registerClientPacket(ResourceLocation id, Class<T> packetClass) {
        //? if < 1.20.5 {
        /^ClientPlayNetworking.registerGlobalReceiver(id, (client, handler, buf, responseSender) -> {
            try {
                T packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(buf);
                Services.SIDE.scheduleClient(packet.getExecutor());
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException ex) {
                Haybale.LOG.error("Error: Failed to instantiate packet - " + id);
            }
        });
        ^///?} else {
        CustomPacketPayload.Type<FabricPacket<T>> type = new CustomPacketPayload.Type<>(id);
        PayloadTypeRegistry.playS2C().register(type, PacketHandler.getCodec(packetClass, id));
        ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            Services.SIDE.scheduleClient(payload.executor());
        });
        //?}
    }

    @Override
    public <T extends Packet> void sendToServer(T packet) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(data);
        //? if < 1.20.5 {
        /^ClientPlayNetworking.send(idMap.get(packet.getClass()), data);
        ^///?} else {
        ResourceLocation id = idMap.get(packet.getClass());
        FabricPacket<T> fabricPacket = new FabricPacket<>(packet, id);
        ClientPlayNetworking.send(fabricPacket);
        //?}
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer player) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(data);
        //? if < 1.20.5 {
        /^ServerPlayNetworking.send(player, idMap.get(packet.getClass()), data);
        ^///?} else {
        ResourceLocation id = idMap.get(packet.getClass());
        FabricPacket<T> fabricPacket = new FabricPacket<>(packet, id);
        ServerPlayNetworking.send(player, fabricPacket);
        //?}
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer... players) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        ResourceLocation id = idMap.get(packet.getClass());
        //? if < 1.20.5 {
        /^packet.encode(data);
        for (ServerPlayer player : players) ServerPlayNetworking.send(player, id, data);
        ^///?} else {
        FabricPacket<T> fabricPacket = new FabricPacket<>(packet, id);
        for (ServerPlayer player : players) ServerPlayNetworking.send(player, fabricPacket);
        //?}
    }

    @Override
    public <T extends Packet> void sendInRange(T packet, Entity e, float range) {
        AABB box = new AABB(e.blockPosition()).inflate(range);
        Level level = VersionHelper.getLevel(e);
        sendInArea(packet, level, box);
    }

    @Override
    public <T extends Packet> void sendInArea(T packet, Level world, AABB area) {
        ServerPlayer[] players = ((ServerLevel) world).players().stream().filter((p) -> area.contains(p.position())).toArray(ServerPlayer[]::new);
        sendTo(packet, players);
    }

}
*///?}