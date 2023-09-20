package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.FabricCommonRegistry;
import com.t2pellet.tlib.common.TLibMod;
import com.t2pellet.tlib.config.ConfigRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public abstract class TLibFabricMod implements ModInitializer, ClientModInitializer {

    private final String modid;
    private final TLibMod commonMod;
    private final TLibModClient clientMod;

    public TLibFabricMod() {
        TLibMod.IMod modAnnotation = getClass().getAnnotation(TLibMod.IMod.class);
        commonMod = getCommonMod();
        clientMod = getClientMod();
        modid = modAnnotation.value();
    }

    protected abstract TLibMod getCommonMod();
    protected abstract TLibModClient getClientMod();

    @Override
    public void onInitializeClient() {
        ClientRegistrar.register(modid, clientMod.entityModels());
        ClientRegistrar.register(modid, clientMod.entityRenderers());
        ClientRegistrar.register(modid, clientMod.particleFactories());
    }

    @Override
    public void onInitialize() {
        ConfigRegistrar.INSTANCE.register(modid, commonMod::config);
        CommonRegistrar.register(modid, commonMod.entities());
        CommonRegistrar.register(modid, commonMod.items());
        CommonRegistrar.register(modid, commonMod.particles());
        CommonRegistrar.register(modid, commonMod.sounds());
        CommonRegistrar.register(modid, commonMod.packets());
        CommonRegistrar.register(modid, commonMod.capabilities());
        registerEvents();
    }

    protected void registerEvents() {}
}
