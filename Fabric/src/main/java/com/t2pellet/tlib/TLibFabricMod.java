package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
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
        ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityModels());
        ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityRenderers());
        ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.particleFactories());
    }

    @Override
    public void onInitialize() {
        ConfigRegistrar.INSTANCE.register(modid, commonMod::config);
        CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.entities());
        CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.items());
        CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.particles());
        CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.sounds());
        CommonRegistrar.INSTANCE.registerPackets(modid, commonMod.packets());
        CommonRegistrar.INSTANCE.registerCapabilities(modid, commonMod.capabilities());
        registerEvents();
    }

    protected void registerEvents() {}
}
