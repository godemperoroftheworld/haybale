package com.t2pellet.haybale;

import com.t2pellet.haybale.client.haybaleModClient;
import com.t2pellet.haybale.client.HaybaleClient;
import com.t2pellet.haybale.services.FabricSidedExecutor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

@haybaleMod.IMod(Haybale.MODID)
public class TenzinLibFabric extends haybaleFabricMod {

    private static MinecraftServer server;

    public static MinecraftServer getServer() {
        return server;
    }

    @Override
    protected haybaleMod getCommonMod() {
        return Haybale.INSTANCE;
    }

    @Override
    protected haybaleModClient getClientMod() {
        return HaybaleClient.INSTANCE;
    }

    @Override
    protected void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> TenzinLibFabric.server = server);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ((FabricSidedExecutor) Services.SIDE).onServerTick(server);
        });
    }
}
