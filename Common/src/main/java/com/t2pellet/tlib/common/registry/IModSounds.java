package com.t2pellet.tlib.common.registry;

import net.minecraft.sounds.SoundEvent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

public interface IModSounds {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ISound {
        String value();
    }

    class TLibSound {
        public final Supplier<SoundEvent> SOUND = null;
    }
}
