package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.client.TenzinLibClient;
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

    private Map<String, TLibForgeMod> modMap;

    @Override
    protected void initialSetup() {
        instance = this;
        modMap = new HashMap<>();
    }

    @Override
    protected TLibMod getCommonMod() {
        return TenzinLib.INSTANCE;
    }

    @Override
    protected TLibModClient getClientMod() {
        return TenzinLibClient.INSTANCE;
    }

    @Override
    protected void registerEvents() {
        MinecraftForge.EVENT_BUS.addListener(((ForgeSidedExecutor) Services.SIDE)::onServerTick);
    }

    public void register(String id, TLibForgeMod mod) {
        modMap.put(id, mod);
    }

    public TLibForgeMod get(String modid) {
        return modMap.get(modid);
    }
}
