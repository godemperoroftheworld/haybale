package com.t2pellet.tlib.common;

import com.t2pellet.tlib.PacketRegistrar;
import com.t2pellet.tlib.common.registry.IModEntities;
import com.t2pellet.tlib.common.registry.IModItems;
import com.t2pellet.tlib.common.registry.IModParticles;
import com.t2pellet.tlib.common.registry.IModSounds;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface TLibMod {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ITLibMod {
        String value();
    }

    IModEntities entities();
    IModItems items();
    IModParticles particles();
    IModSounds sounds();

    void registerPackets(PacketRegistrar registrar);

}
