package com.t2pellet.tlib.services;

import com.t2pellet.tlib.TenzinLibFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FabricSidedExecutor implements ISidedExecutor {

    @Override
    @Environment(EnvType.CLIENT)
    public void scheduleClient(Runnable runnable) {
        net.minecraft.client.Minecraft.getInstance().execute(runnable);
    }

    @Override
    public void scheduleServer(Runnable runnable) {
        TenzinLibFabric.getServer().execute(runnable);
    }
}
