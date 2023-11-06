package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.client.compat.ConfigMenu;
import com.t2pellet.tlib.config.ConfigRegistrar;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public abstract class TLibForgeMod {

    // Mod references
    private final String modid;
    private final TLibMod commonMod;
    private final TLibModClient clientMod;

    // Deferred registries
    public final DeferredRegister<EntityType<?>> ENTITIES;
    public final DeferredRegister<ParticleType<?>> PARTICLES;
    public final DeferredRegister<SoundEvent> SOUNDS;
    public final DeferredRegister<Item> ITEMS;

    public TLibForgeMod() {
        initialSetup();
        TLibMod.IMod modAnnotation = getClass().getAnnotation(TLibMod.IMod.class);
        commonMod = getCommonMod();
        clientMod = getClientMod();
        modid = modAnnotation.value();
        TenzinLibForge.getInstance().register(modid, this);
        // Create deferred registers
        ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, modid);
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, modid);
        PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modid);
        SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modid);
        // Common init
        onCommonSetup();
        // Client init
        onClientSetup();
        // Register into deferred registers
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(bus);
        ITEMS.register(bus);
        PARTICLES.register(bus);
        SOUNDS.register(bus);
        // Events
        registerEvents();
    }

    // If you want to call any custom logic in your mod BEFORE any TLib stuff
    protected void initialSetup() {}

    protected abstract TLibMod getCommonMod();
    protected abstract TLibModClient getClientMod();

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

    @OnlyIn(Dist.CLIENT)
    private void onClientSetup() {
        if (clientMod != null) {
            ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityModels());
            ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.entityRenderers());
            ClientRegistrar.INSTANCE.registerFromClass(modid, clientMod.particleFactories());
        }
        if (Services.PLATFORM.isModLoaded("cloth-config")) {
            ConfigMenu configMenu = new ConfigMenu(modid);
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> configMenu.buildConfigScreen()));
        }
    }



}
