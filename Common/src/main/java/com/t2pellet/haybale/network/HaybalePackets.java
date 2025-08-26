package com.t2pellet.haybale.network;

import com.t2pellet.haybale.network.api.registry.IModPackets;
import com.t2pellet.haybale.network.capability.CapabilityPacket;

public class HaybalePackets implements IModPackets {

    @IPacket(name = "capability", client = true)
    public static final haybalePacket capabilityPacket = new haybalePacket(CapabilityPacket.class);

}
