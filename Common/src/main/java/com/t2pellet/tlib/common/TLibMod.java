package com.t2pellet.tlib.common;

import com.t2pellet.tlib.TenzinLib;
import com.t2pellet.tlib.common.entity.capability.IModCapabilities;
import com.t2pellet.tlib.common.network.IModPackets;
import com.t2pellet.tlib.common.registry.*;
import com.t2pellet.tlib.config.Config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class TLibMod {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface IMod {
        String value();
    }

    public TLibMod() {
        IMod modInfo = getClass().getAnnotation(IMod.class);
        if (modInfo != null) {
            TenzinLib.INSTANCE.register(modInfo.value(), this);
        }
    }

    public IModEntities entities() { return null; }
    public IModItems items() { return null; }
    public IModParticles particles() { return null; }
    public IModSounds sounds() { return null; }
    public IModPackets packets() { return null; }
    public IModCapabilities capabilities() { return null; }
    public Config config() { return null; }

}
