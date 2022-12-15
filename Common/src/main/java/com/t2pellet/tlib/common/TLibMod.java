package com.t2pellet.tlib.common;

import com.t2pellet.tlib.TenzinLib;
import com.t2pellet.tlib.common.registry.*;

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

    public abstract IModEntities entities();
    public abstract IModItems items();
    public abstract IModParticles particles();
    public abstract IModSounds sounds();
    public abstract IModPackets packets();

}
