package com.t2pellet.haybale.services;

import com.t2pellet.haybale.TenzinLibFabric;
import net.minecraft.server.MinecraftServer;

public class FabricServerHelper implements IServerHelper {

    @Override
    public MinecraftServer getServer() {
        return TenzinLibFabric.getServer();
    }
}
