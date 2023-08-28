package com.t2pellet.tlib.services;

import com.t2pellet.tlib.TenzinLibFabric;
import net.minecraft.server.MinecraftServer;

public class FabricServerHelper implements IServerHelper {

    @Override
    public MinecraftServer getServer() {
        return TenzinLibFabric.getServer();
    }
}
