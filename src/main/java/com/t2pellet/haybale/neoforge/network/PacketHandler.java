//? if neoforge {
package com.t2pellet.haybale.neoforge.network;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.services.IPacketHandler;
import com.t2pellet.haybale.common.network.api.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;

public class PacketHandler implements IPacketHandler {

    private final String PROTOCOL_VERSION = "4";
    private final Map<String, Map<Class<? extends Packet>, ResourceLocation>> packetMap = new HashMap<>();
    private final Map<Class<? extends Packet>, ResourceLocation> packetFlatMap = new HashMap<>();

    private interface PacketPayloadWithExecutor extends CustomPacketPayload {
        Runnable getExecutor();
    }

    public void registerPackets(RegisterPayloadHandlerEvent event) {
        Set<String> modIDs = packetMap.keySet();
        modIDs.forEach(modID -> {
            final IPayloadRegistrar registrar = event.registrar(modID)
                    .versioned(PROTOCOL_VERSION)
                    .optional();
            final Map<Class<? extends Packet>, ResourceLocation> packetClasses = packetMap.get(modID);

            packetClasses.forEach((packetClass, packetID) -> {
                registrar.<PacketPayloadWithExecutor>play(packetID, friendlyByteBuf -> {
                    try {
                        Packet packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(friendlyByteBuf);
                        return new PacketPayloadWithExecutor() {
                            @Override
                            public Runnable getExecutor() {
                                return packet.getExecutor();
                            }

                            @Override
                            public void write(FriendlyByteBuf arg) {
                                packet.encode(arg);
                            }

                            @Override
                            public ResourceLocation id() {
                                return packetID;
                            }
                        };
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException ex) {
                        Haybale.LOG.error("Error: Failed to instantiate packet - " + packetID);
                    }
                    return null;
                }, (t, contextSupplier) -> {
                    contextSupplier.workHandler().submitAsync(t.getExecutor());
                });
            });
        });
    }

    public void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass) {
        Map<Class<? extends Packet>, ResourceLocation> packetClasses = packetMap.getOrDefault(modid, new HashMap<>());
        ResourceLocation id = new ResourceLocation(modid, name);
        packetClasses.put(packetClass, id);
        packetFlatMap.put(packetClass, id);

    }

    public void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass) {
        Map<Class<? extends Packet>, ResourceLocation> packetClasses = packetMap.getOrDefault(modid, new HashMap<>());
        ResourceLocation id = new ResourceLocation(modid, name);
        packetClasses.put(packetClass, id);
        packetFlatMap.put(packetClass, id);
    }

    @Override
    public <T extends Packet> void sendToServer(T packet) {
        PacketDistributor.SERVER.noArg().send(new CustomPacketPayload() {
            @Override
            public void write(FriendlyByteBuf arg) {
                packet.encode(arg);
            }

            @Override
            public ResourceLocation id() {
                return packetFlatMap.get(packet.getClass());
            }
        });
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(new CustomPacketPayload() {
            @Override
            public void write(FriendlyByteBuf arg) {
                packet.encode(arg);
            }

            @Override
            public ResourceLocation id() {
                return packetFlatMap.get(packet.getClass());
            }
        });
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
        sendInArea(packet, e.level(), box);
    }

    @Override
    public <T extends Packet> void sendInArea(T packet, Level world, AABB area) {
        ServerPlayer[] players = ((ServerLevel) world).players().stream().filter((p) -> area.contains(p.position())).toArray(ServerPlayer[]::new);
        sendTo(packet, players);
    }
}
//?}