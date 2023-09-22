package com.t2pellet.tlib.network;

import com.t2pellet.tlib.network.api.registry.IModPackets;
import com.t2pellet.tlib.network.capability.CapabilityPacket;

public class TLibPackets implements IModPackets {

    @IPacket(name = "capability", client = true)
    public static final TLibPacket capabilityPacket = new TLibPacket(CapabilityPacket.class);

}
