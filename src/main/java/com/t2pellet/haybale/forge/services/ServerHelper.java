//? if forge {
/*package com.t2pellet.haybale.forge.services;

import com.t2pellet.haybale.services.IServerHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ServerHelper implements IServerHelper {
    @Override
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }
}
*///?}