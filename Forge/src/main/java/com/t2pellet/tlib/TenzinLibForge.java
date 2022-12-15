package com.t2pellet.tlib;

import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod(TenzinLib.MODID)
public class TenzinLibForge {

    private static TenzinLibForge instance = null;
    public static TenzinLibForge getInstance() {
        return instance;
    }

    private final Map<String, TLibForgeMod> modMap = new HashMap<>();

    public TenzinLibForge() {
        TenzinLibForge.instance = this;
    }

    public void register(String id, TLibForgeMod mod) {
        modMap.put(id, mod);
    }

    public TLibForgeMod get(String modid) {
        return modMap.get(modid);
    }

}
