//? if fabric {
package com.t2pellet.haybale.fabric.services;

import com.t2pellet.haybale.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class PlatformHelper implements IPlatformHelper {

    @Override
    public String getGameDir() {
        return FabricLoader.getInstance().getGameDir().toString();
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
//?}