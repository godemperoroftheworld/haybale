//? if neoforge {
package com.t2pellet.haybale.neoforge;

import com.t2pellet.haybale.common.ClientRegistrar;
import com.t2pellet.haybale.common.CommonRegistrar;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.client.compat.ConfigMenu;
import com.t2pellet.haybale.common.config.ConfigRegistrar;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class HaybaleNeoforgeMod {

    // Mod references
    private final String modid;
    private final HaybaleMod commonMod;
    private final HaybaleModClient clientMod;

    // Deferred registries
    public final net.neoforged.neoforge.registries.DeferredRegister<EntityType<?>> ENTITIES;
    public final DeferredRegister<ParticleType<?>> PARTICLES;
    public final DeferredRegister<SoundEvent> SOUNDS;
    public final DeferredRegister<Item> ITEMS;

    public HaybaleNeoforgeMod() {
        initialSetup();
        HaybaleMod.IMod modAnnotation = getClass().getAnnotation(HaybaleMod.IMod.class);
        commonMod = getCommonMod();
        clientMod = getClientMod();
        modid = modAnnotation.value();
        HaybaleNeoforge.getInstance().register(modid, this);
        // Create deferred registers
        ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, modid);
        ITEMS = DeferredRegister.create(Registries.ITEM, modid);
        PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, modid);
        SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, modid);
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
        registerEvents(bus);
    }

    // If you want to call any custom logic in your mod BEFORE any haybale stuff
    protected void initialSetup() {}

    protected abstract HaybaleMod getCommonMod();
    protected abstract HaybaleModClient getClientMod();

    protected void registerEvents(IEventBus bus) {
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
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> configMenu.buildConfigScreen()));
        }
    }
}
//?}
