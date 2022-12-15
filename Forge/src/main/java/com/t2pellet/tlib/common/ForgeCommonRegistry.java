package com.t2pellet.tlib.common;

import com.t2pellet.tlib.TLibForgeMod;
import com.t2pellet.tlib.TenzinLibForge;
import com.t2pellet.tlib.common.registry.ICommonRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ForgeCommonRegistry implements ICommonRegistry {

    @Override
    public Supplier<SimpleParticleType> registerParticle(String modid, String name) {
        TLibForgeMod forgeMod = TenzinLibForge.getInstance().get(modid);
        forgeMod.PARTICLES.register(name, () -> new SimpleParticleType(true));
        return null;
    }

    @Override
    public <T extends LivingEntity> Supplier<EntityType<T>> registerEntity(String modid, String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, AttributeSupplier.Builder builder) {
        TLibForgeMod forgeMod = TenzinLibForge.getInstance().get(modid);
        RegistryObject<EntityType<T>> result = forgeMod.ENTITIES.register(name, () -> EntityType.Builder.of(factory, category)
                .clientTrackingRange(48).updateInterval(3).sized(width, height)
                .build(name));
        FMLJavaModLoadingContext.get().getModEventBus().addListener((Consumer<EntityAttributeCreationEvent>) event -> event.put(result.get(), builder.build()));
        return result;
    }

    @Override
    public Supplier<SoundEvent> registerSound(String modid, String name) {
        TLibForgeMod forgeMod = TenzinLibForge.getInstance().get(modid);
        SoundEvent sound = new SoundEvent(new ResourceLocation(modid, name));
        return forgeMod.SOUNDS.register(name, () -> sound);
    }

    @Override
    public Supplier<Item> registerItem(String modid, String name, Item.Properties properties) {
        TLibForgeMod forgeMod = TenzinLibForge.getInstance().get(modid);
        return forgeMod.ITEMS.register(name, () -> new Item(properties));
    }

    @Override
    public void registerEvents() {
    }
}
