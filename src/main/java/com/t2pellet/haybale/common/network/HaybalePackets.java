package com.t2pellet.haybale.common.network;

import com.t2pellet.haybale.common.network.api.registry.IModPackets;
import com.t2pellet.haybale.common.network.capability.CapabilityPacket;

public class HaybalePackets implements IModPackets {

    @IPacket(name = "capability", client = true)
    public static final HaybalePacket capabilityPacket = new HaybalePacket(CapabilityPacket.class);

}
