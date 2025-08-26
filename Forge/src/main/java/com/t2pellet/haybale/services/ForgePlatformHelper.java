package com.t2pellet.haybale.services;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getGameDir() {
        return FMLPaths.GAMEDIR.get().toString();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
