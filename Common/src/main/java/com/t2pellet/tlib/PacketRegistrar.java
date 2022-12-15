package com.t2pellet.tlib;

import com.t2pellet.tlib.common.network.Packet;

public interface PacketRegistrar {

    PacketRegistrar INSTANCE = new PacketRegistrar() {
        @Override
        public <T extends Packet<T>> void registerPacket(Class<T> packetClass) {
        }

        @Override
        public <T extends Packet<T>> void registerClientPacket(Class<T> packetClass) {
        }
    };

    <T extends Packet<T>> void registerPacket(Class<T> packetClass);
    <T extends Packet<T>> void registerClientPacket(Class<T> packetClass);

}
