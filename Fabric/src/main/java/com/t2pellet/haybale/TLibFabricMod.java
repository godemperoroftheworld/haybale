package com.t2pellet.haybale;

import com.t2pellet.haybale.client.haybaleModClient;
import com.t2pellet.haybale.config.ConfigRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public abstract class haybaleFabricMod implements ModInitializer, ClientModInitializer {

    private final String modid;
    private final haybaleMod commonMod;
    private final haybaleModClient clientMod;

    public haybaleFabricMod() {
        haybaleMod.IMod modAnnotation = getClass().getAnnotation(haybaleMod.IMod.class);
        commonMod = getCommonMod();
        clientMod = getClientMod();
        modid = modAnnotation.value();
    }

    protected abstract haybaleMod getCommonMod();
    protected abstract haybaleModClient getClientMod();

    @Override
    public void onInitializeClient() {
        ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.particleFactories());
        ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityModels());
        ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityRenderers());
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
