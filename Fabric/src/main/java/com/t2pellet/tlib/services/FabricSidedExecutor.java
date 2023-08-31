package com.t2pellet.tlib.services;

import com.t2pellet.tlib.TenzinLibFabric;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

import java.util.PriorityQueue;

public class FabricSidedExecutor implements ISidedExecutor {

    static class PQEntry implements Comparable<PQEntry> {

        private final Runnable runnable;
        private final Long tick;

        public PQEntry(Runnable runnable, long tick) {
            this.runnable = runnable;
            this.tick = tick;
        }

        @Override
        public int compareTo(@NotNull FabricSidedExecutor.PQEntry o) {
            return tick.compareTo(o.tick);
        }
    }

    private final PriorityQueue<PQEntry> pq = new PriorityQueue<>();
    private long tick = 0;

    @Override
    @Environment(EnvType.CLIENT)
    public void scheduleClient(Runnable runnable) {
        net.minecraft.client.Minecraft.getInstance().execute(runnable);
    }

    @Override
    public void scheduleServer(Runnable runnable) {
        TenzinLibFabric.getServer().execute(runnable);
    }

    @Override
    public void scheduleServer(int ticks, Runnable runnable) {
        pq.add(new PQEntry(runnable, tick + ticks));
    }

    public void onServerTick(MinecraftServer server) {
        ++tick;
        PQEntry top = pq.peek();
        if (top != null && top.tick >= tick) {
            pq.poll();
            server.execute(top.runnable);
        }
    }

}
