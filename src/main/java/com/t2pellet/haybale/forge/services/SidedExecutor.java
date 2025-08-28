//? if forge {
package com.t2pellet.haybale.forge.services;

import com.t2pellet.haybale.services.ISidedExecutor;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;

public class SidedExecutor implements ISidedExecutor {

    static class PQEntry implements Comparable<PQEntry> {

        private final Runnable runnable;
        private final Long tick;

        public PQEntry(Runnable runnable, long tick) {
            this.runnable = runnable;
            this.tick = tick;
        }

        @Override
        public int compareTo(@NotNull SidedExecutor.PQEntry o) {
            return tick.compareTo(o.tick);
        }
    }

    private final PriorityQueue<PQEntry> pq = new PriorityQueue<>();
    private long tick = 0;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void scheduleClient(Runnable runnable) {
        net.minecraft.client.Minecraft.getInstance().execute(runnable);
    }

    @Override
    public void scheduleServer(Runnable runnable) {
        ServerLifecycleHooks.getCurrentServer().execute(runnable);
    }

    @Override
    public void scheduleServer(int ticks, Runnable runnable) {
        pq.add(new PQEntry(runnable, tick + ticks));
    }

    public void onServerTick(TickEvent.ServerTickEvent event) {
        ++tick;
        PQEntry top = pq.peek();
        if (top != null && top.tick >= tick) {
            pq.poll();
            //? if <= 1.18.2 {
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            //?} else
            /*MinecraftServer server = event.getServer();*/
            server.execute(top.runnable);
        }
    }
}
//?}