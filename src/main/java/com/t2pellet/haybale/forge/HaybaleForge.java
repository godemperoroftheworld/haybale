//? if forge {
/*package com.t2pellet.haybale.forge;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.client.HaybaleClient;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.forge.services.SidedExecutor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod(Haybale.MODID)
@HaybaleMod.IMod(Haybale.MODID)
public class HaybaleForge extends HaybaleForgeMod {

    private static HaybaleForge instance = null;
    public static HaybaleForge getInstance() {
        return instance;
    }

    private Map<String, HaybaleForgeMod> modMap;

    @Override
    protected void initialSetup() {
        instance = this;
        modMap = new HashMap<>();
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
        MinecraftForge.EVENT_BUS.addListener(((SidedExecutor) Services.SIDE)::onServerTick);
    }

    public void register(String id, HaybaleForgeMod mod) {
        modMap.put(id, mod);
    }

    public HaybaleForgeMod get(String modid) {
        return modMap.get(modid);
    }
}
*///?}