package com.t2pellet.haybale.common.network.api.registry;

import com.t2pellet.haybale.common.network.api.Packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IModPackets {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IPacket {
        String name();
        boolean client();
    }

    class HaybalePacket {

        private final Class<? extends Packet> packetClass;

        public HaybalePacket(Class<? extends Packet> packetClass) {
            this.packetClass = packetClass;
        }

        public Class<? extends Packet> getPacketClass() {
            return packetClass;
        }
    }
}
