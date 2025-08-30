package com.t2pellet.haybale.services;

import net.minecraft.network.chat.Component;
//? if <= 1.18.2 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///?}
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class VersionHelper {

    public Level getLevel(Entity entity) {
        //? if <=1.19.4 {
        /*return entity.getLevel();
        *///?} else
        return entity.level();
    }

    public Component translatableComponent(String key) {
        //? if <= 1.18.2 {
        /*return new TranslatableComponent(key);
        *///?} else
        return Component.translatable(key);
    }

    public Component literalComponent(String key) {
        //? if <= 1.18.2 {
        /*return new TextComponent(key);
        *///?} else
        return Component.literal(key);
    }

    public ResourceLocation getResourceLocation(String modid, String name) {
        //? if < 1.21 {
        /*return new ResourceLocation(modid, name);
        *///?} else
        return ResourceLocation.fromNamespaceAndPath(modid, name);
    }
}
