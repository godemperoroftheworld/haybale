package com.t2pellet.tlib.common.network;

public interface ICommonPacketHandler {

    <T extends Packet<T>> void registerPacket(String id, Class<T> packetClass);

}
