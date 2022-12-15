package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.TLibMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public abstract class TLibFabricMod implements ModInitializer, ClientModInitializer {

    private final String modid;
    private final TLibMod commonMod;
    private final TLibModClient clientMod;

    public TLibFabricMod() {
        TLibMod.IMod modAnnotation = getClass().getAnnotation(TLibMod.IMod.class);
        commonMod = TenzinLib.INSTANCE.get(modAnnotation.value());
        clientMod = TenzinLib.INSTANCE.getClient(modAnnotation.value());
        modid = modAnnotation.value();
    }

    @Override
    public void onInitializeClient() {
        ClientRegistrar.register(modid, clientMod.entityModels());
        ClientRegistrar.register(modid, clientMod.particleFactories());
    }

    @Override
    public void onInitialize() {
        CommonRegistrar.register(modid, commonMod.entities());
        CommonRegistrar.register(modid, commonMod.items());
        CommonRegistrar.register(modid, commonMod.particles());
        CommonRegistrar.register(modid, commonMod.sounds());
        CommonRegistrar.register(modid, commonMod.packets());
    }
}
