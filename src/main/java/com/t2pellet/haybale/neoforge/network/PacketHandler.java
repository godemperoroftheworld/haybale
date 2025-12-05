//? if neoforge {
package com.t2pellet.haybale.neoforge.network;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.utils.VersionHelper;
import com.t2pellet.haybale.services.IPacketHandler;
import com.t2pellet.haybale.common.network.api.Packet;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
//? if < 1.20.5 {
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.NotNull;
//?} else {
/*import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;
import net.minecraft.network.codec.StreamCodec;
*///?}

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;


public class PacketHandler implements IPacketHandler {

    //? if >= 1.20.5 {
    /*private static <T extends Packet> StreamCodec<FriendlyByteBuf, NeoforgePacket<T>> getCodec(Class<T> packetClass, ResourceLocation id) {
        return StreamCodec.of(
                (packetByteBuf, fabricPacket) -> {
                    // Encode
                    fabricPacket.encode(packetByteBuf);
                },
                (packetByteBuf) -> {
                    // Decode
                    try {
                        T packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(packetByteBuf);
                        return new NeoforgePacket<>(packet, id);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException ex) {
                        Haybale.LOG.error("Error: Failed to instantiate packet - " + id);
                    }
                    return null;
                }
        );
    }
    *///?}
    private static class NeoforgePacket<T extends Packet> implements CustomPacketPayload {

        private final T packet;
        //? if >= 1.20.5 {
        /*private final Type<NeoforgePacket<T>> type;
        *///?} else {
        private final ResourceLocation id;
        //?}

        public NeoforgePacket(T packet, ResourceLocation id) {
            this.packet = packet;
            //? if >= 1.20.5 {
            /*this.type = new CustomPacketPayload.Type<>(id);
            *///?} else {
            this.id = id;
            //?}
        }

        //? if >= 1.20.5 {
        /*@Override
        public @NotNull Type<NeoforgePacket<T>> type() {
            return this.type;
        }
        *///?} else {
        @Override
        public @NotNull ResourceLocation id() {
            return this.id;
        }
        //?}

        //? if < 1.20.5 {
        @Override
        public void write(@NotNull FriendlyByteBuf packetByteBuf) {
        //?} else {
        /*public void encode(FriendlyByteBuf packetByteBuf) {
        *///?}
            this.packet.encode(packetByteBuf);
        }

        public Runnable executor() {
            return this.packet.getExecutor();
        }
    }


    private final String PROTOCOL_VERSION = "4";
    private final Map<String, Map<Class<? extends Packet>, ResourceLocation>> packetMap = new HashMap<>();
    private final Map<Class<? extends Packet>, ResourceLocation> packetFlatMap = new HashMap<>();

    public void registerPackets(
            //? if < 1.20.5 {
            RegisterPayloadHandlerEvent event
            //?} else
            /*RegisterPayloadHandlersEvent event*/
    ) {
        Set<String> modIDs = packetMap.keySet();
        modIDs.forEach(modID -> {
            //? if < 1.20.5 {
            final IPayloadRegistrar registrar = event.registrar(modID).versioned(PROTOCOL_VERSION)
            //?} else
            /*final PayloadRegistrar registrar = event.registrar(modID)*/
                    .versioned(PROTOCOL_VERSION);
            final Map<Class<? extends Packet>, ResourceLocation> packetClasses = packetMap.get(modID);

            packetClasses.forEach((packetClass, packetID) -> {
                //? if < 1.20.5 {
                registrar.play(packetID, friendlyByteBuf -> {
                    try {
                        Packet packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(friendlyByteBuf);
                        return new NeoforgePacket<>(packet, packetID);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                             InvocationTargetException ex) {
                        Haybale.LOG.error("Error: Failed to instantiate packet - {}", packetID);
                    }
                    return null;
                }, (t, contextSupplier) -> {
                    contextSupplier.workHandler().submitAsync(t.executor());
                });
                //?} else {
                /*CustomPacketPayload.Type<NeoforgePacket<Packet>> type = new CustomPacketPayload.Type<>(packetID);
                registrar.playBidirectional(type, StreamCodec.of(
                        (packetByteBuf, packet) -> {
                            // Encode
                            packet.encode(packetByteBuf);
                        },
                        packetByteBuf -> {
                            // Decode
                            try {
                                Packet packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(packetByteBuf);
                                return new NeoforgePacket<>(packet, packetID);
                            } catch (ReflectiveOperationException e) {
                                Haybale.LOG.error("Error: Failed to instantiate packet - " + packetID);
                            }
                            return null;
                        }),
                        (packet, context) -> {
                            // Execute
                            context.enqueueWork(packet.executor());
                        }
                );
                *///?}
            });
        });
    }

    public void registerServerPacket(String modid, String name, Class<? extends Packet> packetClass) {
        ResourceLocation id = VersionHelper.getResourceLocation(modid, name);
        registerPacket(modid, packetClass, id);

    }

    public void registerClientPacket(String modid, String name, Class<? extends Packet> packetClass) {
        ResourceLocation id = VersionHelper.getResourceLocation(modid, name);
        registerPacket(modid, packetClass, id);
    }

    private void registerPacket(String modid, Class<? extends Packet> packetClass, ResourceLocation id) {
        if (packetMap.containsKey(modid)) {
            packetMap.get(modid).put(packetClass, id);
        } else {
            Map<Class<? extends Packet>, ResourceLocation> map = new HashMap<>();
            map.put(packetClass, id);
            packetMap.put(modid, map);
        }
        packetFlatMap.put(packetClass, id);
    }

    @Override
    public <T extends Packet> void sendToServer(T packet) {
        ResourceLocation id = packetFlatMap.get(packet.getClass());
        //? if >= 1.20.5 {
        /*PacketDistributor.sendToServer(new NeoforgePacket<>(packet, id));
        *///?} else {
        NeoforgePacket<T> neoforgePacket = new NeoforgePacket<>(packet, id);
        // Dirty hack, we need to force serialisation in 1.20.4 on an integrated server
        if (!Services.SERVER_HELPER.getServer().isDedicatedServer()) {
            neoforgePacket.write(new FriendlyByteBuf(Unpooled.buffer()));
        }
        PacketDistributor.SERVER.noArg().send(neoforgePacket);
        //?}
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer player) {
        ResourceLocation id = packetFlatMap.get(packet.getClass());
        //? if >= 1.20.5 {
        /*PacketDistributor.sendToPlayer(player, new NeoforgePacket<>(packet, id));
        *///?} else {
        NeoforgePacket<T> neoforgePacket = new NeoforgePacket<>(packet, id);
        // Dirty hack, we need to force serialisation in 1.20.4 on an integrated server
        if (!Services.SERVER_HELPER.getServer().isDedicatedServer()) {
            neoforgePacket.write(new FriendlyByteBuf(Unpooled.buffer()));
        }
        PacketDistributor.PLAYER.with(player).send(neoforgePacket);
        //?}
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