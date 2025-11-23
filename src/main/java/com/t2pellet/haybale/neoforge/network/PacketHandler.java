//? if neoforge {
package com.t2pellet.haybale.neoforge.network;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.common.utils.VersionHelper;
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

import javax.management.ReflectionException;
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

    private static class NeoforgePacket<T extends Packet> implements CustomPacketPayload {

        private final T packet;
        private final Type<NeoforgePacket<T>> type;

        public NeoforgePacket(T packet, ResourceLocation id) {
            this.packet = packet;
            this.type = new CustomPacketPayload.Type<>(id);
        }

        @Override
        public @NotNull Type<NeoforgePacket<T>> type() {
            return this.type;
        }

        public void encode(FriendlyByteBuf packetByteBuf) {
            this.packet.encode(packetByteBuf);
        }

        public Runnable executor() {
            return this.packet.getExecutor();
        }
    }
    *///?} else {
    private interface PacketPayloadWithExecutor extends CustomPacketPayload {
        Runnable getExecutor();
    }
    //?}


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
            final IPayloadRegistrar registrar = event.registrar(modID)
            //?} else
            /*final PayloadRegistrar registrar = event.registrar(modID)*/
                    .versioned(PROTOCOL_VERSION)
                    .optional();
            final Map<Class<? extends Packet>, ResourceLocation> packetClasses = packetMap.get(modID);

            packetClasses.forEach((packetClass, packetID) -> {
                //? if < 1.20.5 {
                registrar.<PacketPayloadWithExecutor>play(packetID, friendlyByteBuf -> {
                    try {
                        Packet packet = packetClass.getDeclaredConstructor(FriendlyByteBuf.class).newInstance(friendlyByteBuf);
                        return new PacketPayloadWithExecutor() {
                            @Override
                            public Runnable getExecutor() {
                                return packet.getExecutor();
                            }

                            @Override
                            public void write(@NotNull FriendlyByteBuf arg) {
                                packet.encode(arg);
                            }

                            @Override
                            public @NotNull ResourceLocation id() {
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
        //? if >= 1.20.5 {
        /*ResourceLocation id = packetFlatMap.get(packet.getClass());
        PacketDistributor.sendToServer(new NeoforgePacket<>(packet, id));
        *///?} else {
        PacketDistributor.SERVER.noArg().send(new PacketPayloadWithExecutor() {
            @Override
            public Runnable getExecutor() {
                return packet.getExecutor();
            }

            @Override
            public void write(FriendlyByteBuf arg) {
                packet.encode(arg);
            }

            @Override
            public @NotNull ResourceLocation id() {
                return packetFlatMap.get(packet.getClass());
            }
        });
        //?}
    }

    @Override
    public <T extends Packet> void sendTo(T packet, ServerPlayer player) {
        //? if >= 1.20.5 {
        /*ResourceLocation id = packetFlatMap.get(packet.getClass());
        PacketDistributor.sendToPlayer(player, new NeoforgePacket<>(packet, id));
        *///?} else {
        PacketDistributor.PLAYER.with(player).send(new PacketPayloadWithExecutor() {
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
                return packetFlatMap.get(packet.getClass());
            }
        });
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