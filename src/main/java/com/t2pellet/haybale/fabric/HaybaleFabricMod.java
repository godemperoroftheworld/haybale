//? if fabric {
package com.t2pellet.haybale.fabric;

import com.t2pellet.haybale.common.ClientRegistrar;
import com.t2pellet.haybale.common.CommonRegistrar;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.common.config.ConfigRegistrar;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public abstract class HaybaleFabricMod implements ModInitializer, ClientModInitializer {

    private final String modid;
    private final HaybaleMod commonMod;
    private final HaybaleModClient clientMod;

    public HaybaleFabricMod() {
        HaybaleMod.IMod modAnnotation = getClass().getAnnotation(HaybaleMod.IMod.class);
        commonMod = getCommonMod();
        clientMod = getClientMod();
        modid = modAnnotation.value();
    }

    protected abstract HaybaleMod getCommonMod();
    protected abstract HaybaleModClient getClientMod();

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
//?}