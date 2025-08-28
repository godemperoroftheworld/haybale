package com.t2pellet.haybale.services;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class VersionHelper {

    public Level getLevel(Entity entity) {
        //? if <=1.19.4 {
        return entity.getLevel();
        //?} else
        /*return entity.level();*/
    }
}
