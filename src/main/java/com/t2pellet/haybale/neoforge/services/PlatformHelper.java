//? if neoforge {
/*package com.t2pellet.haybale.neoforge.services;

import com.t2pellet.haybale.services.IPlatformHelper;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;

public class PlatformHelper implements IPlatformHelper {

    @Override
    public String getGameDir() {
        return FMLPaths.GAMEDIR.get().toString();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }
}
*///?}