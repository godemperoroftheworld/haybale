//? if fabric {
package com.t2pellet.haybale.fabric;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.client.HaybaleClient;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.fabric.services.SidedExecutor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

@HaybaleMod.IMod(Haybale.MODID)
public class HaybaleFabric extends HaybaleFabricMod {

    private static MinecraftServer server;

    public static MinecraftServer getServer() {
        return server;
    }

    @Override
    protected HaybaleMod getCommonMod() {
        return Haybale.INSTANCE;
    }

    @Override
    protected HaybaleModClient getClientMod() {
        return HaybaleClient.INSTANCE;
    }

    @Override
    protected void registerEvents() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> HaybaleFabric.server = server);
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ((SidedExecutor) Services.SIDE).onServerTick(server);
        });
    }
}
//?}