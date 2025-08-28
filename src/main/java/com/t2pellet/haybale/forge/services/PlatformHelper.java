//? if forge {
package com.t2pellet.haybale.forge.services;

import com.t2pellet.haybale.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

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
//?}