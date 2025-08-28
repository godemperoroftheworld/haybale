//? if forge {
package com.t2pellet.haybale.forge;

import com.t2pellet.haybale.common.ClientRegistrar;
import com.t2pellet.haybale.common.CommonRegistrar;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.client.compat.ConfigMenu;
import com.t2pellet.haybale.common.config.ConfigRegistrar;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
//? if > 1.18.2 {
/*import net.minecraftforge.client.ConfigScreenHandler;
*///?} else
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class HaybaleForgeMod {

    // Mod references
    private final String modid;
    private final HaybaleMod commonMod;
    private final HaybaleModClient clientMod;

    // Deferred registries
    public final DeferredRegister<EntityType<?>> ENTITIES;
    public final DeferredRegister<ParticleType<?>> PARTICLES;
    public final DeferredRegister<SoundEvent> SOUNDS;
    public final DeferredRegister<Item> ITEMS;

    public HaybaleForgeMod() {
        initialSetup();
        HaybaleMod.IMod modAnnotation = getClass().getAnnotation(HaybaleMod.IMod.class);
        commonMod = getCommonMod();
        clientMod = getClientMod();
        modid = modAnnotation.value();
        HaybaleForge.getInstance().register(modid, this);
        // Create deferred registers
        ENTITIES = DeferredRegister.create(
                //? if > 1.18.2 {
                /*ForgeRegistries.ENTITY_TYPES,
                *///?} else
                ForgeRegistries.ENTITIES,
                modid
        );
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modid);
        SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modid);
        // Common init
        onCommonSetup();
        // Client init
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::onClientSetup);
        // Register into deferred registers
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(bus);
        ITEMS.register(bus);
        PARTICLES.register(bus);
        SOUNDS.register(bus);
        // Events
        registerEvents();
    }

    // If you want to call any custom logic in your mod BEFORE any haybale stuff
    protected void initialSetup() {}

    protected abstract HaybaleMod getCommonMod();
    protected abstract HaybaleModClient getClientMod();

    protected void registerEvents() {
    }

    private void onCommonSetup() {
        if (commonMod != null) {
            CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.particles());
            CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.entities());
            CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.items());
            CommonRegistrar.INSTANCE.registerFromClass(modid, commonMod.sounds());
            CommonRegistrar.INSTANCE.registerPackets(modid, commonMod.packets());
            CommonRegistrar.INSTANCE.registerCapabilities(modid, commonMod.capabilities());
            ConfigRegistrar.INSTANCE.register(modid, commonMod::config);
        }
    }

    private void onClientSetup() {
        if (clientMod != null) {
            ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityModels());
            ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityRenderers());
            ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.particleFactories());
        }
        // I have no idea why their modid is different in forge
        if (Services.PLATFORM.isModLoaded("cloth_config")) {
            ConfigMenu configMenu = new ConfigMenu(modid);
            ModLoadingContext.get().registerExtensionPoint(
                    //? if > 1.18.2 {
                    /*ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> configMenu.buildConfigScreen())
                    *///?} else {
                    ConfigGuiHandler.ConfigGuiFactory.class,
                    () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> configMenu.buildConfigScreen())
                    //?}
            );
        }
    }
}
//?}
