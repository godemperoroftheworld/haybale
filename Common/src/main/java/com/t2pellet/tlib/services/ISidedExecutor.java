package com.t2pellet.tlib.services;

public interface ISidedExecutor {

    void scheduleClient(Runnable runnable);

    void scheduleServer(Runnable runnable);

    void scheduleServer(int ticks, Runnable runnable);

}
