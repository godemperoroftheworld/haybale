package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.TLibMod;
import com.t2pellet.tlib.services.ForgeSidedExecutor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod(TenzinLib.MODID)
@TLibMod.IMod(TenzinLib.MODID)
public class TenzinLibForge extends TLibForgeMod {

    private static TenzinLibForge instance = null;
    public static TenzinLibForge getInstance() {
        return instance;
    }

    private final Map<String, TLibForgeMod> modMap = new HashMap<>();

    public TenzinLibForge() {
        TenzinLibForge.instance = this;
    }

    @Override
    protected TLibMod getCommonMod() {
        return TenzinLib.INSTANCE;
    }

    @Override
    protected TLibModClient getClientMod() {
        return null;
    }

    public void register(String id, TLibForgeMod mod) {
        modMap.put(id, mod);
    }

    public TLibForgeMod get(String modid) {
        return modMap.get(modid);
    }

    @Override
    protected void registerEvents() {
        MinecraftForge.EVENT_BUS.addListener(((ForgeSidedExecutor) Services.SIDE)::onServerTick);
    }
}
