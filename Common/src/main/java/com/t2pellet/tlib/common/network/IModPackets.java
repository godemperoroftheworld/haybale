package com.t2pellet.tlib.common.network;

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

     class TLibPacket<T extends Packet<T>> {
        public final Class<T> PACKET;

        public TLibPacket(Class<T> packet) {
            PACKET = packet;
        }
    }
}
