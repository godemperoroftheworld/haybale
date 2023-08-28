package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.TLibMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

@TLibMod.IMod(TenzinLib.MODID)
public class TenzinLibFabric extends TLibFabricMod {

    private static MinecraftServer server;

    public static MinecraftServer getServer() {
        return server;
    }

    @Override
    protected TLibMod getCommonMod() {
        return TenzinLib.INSTANCE;
    }

    @Override
    protected TLibModClient getClientMod() {
        return null;
    }

    @Override
    protected void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> TenzinLibFabric.server = server);
    }
}
