package com.t2pellet.tlib.client.network;

import com.t2pellet.tlib.common.network.Packet;

public interface IClientPacketHandler {

    <T extends Packet<T>> void registerPacket(String id, Class<T> packetClass);

}
