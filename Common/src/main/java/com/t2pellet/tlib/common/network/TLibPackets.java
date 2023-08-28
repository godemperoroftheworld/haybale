package com.t2pellet.tlib.common.network;

import com.t2pellet.tlib.common.network.capability.CapabilityPacket;

public class TLibPackets implements IModPackets {


    @IPacket(name = "capability", client = true)
    public static final TLibPacket capabilityPacket = new TLibPacket(CapabilityPacket.class);

}
