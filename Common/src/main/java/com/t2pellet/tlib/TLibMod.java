package com.t2pellet.tlib;

import com.t2pellet.tlib.entity.capability.api.registry.IModCapabilities;
import com.t2pellet.tlib.network.api.registry.IModPackets;
import com.t2pellet.tlib.config.api.Config;
import com.t2pellet.tlib.registry.api.RegistryClass;

import java.io.IOException;
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

    public Class<? extends RegistryClass> entities() { return null; }
    public Class<? extends RegistryClass> items() { return null; }
    public Class<? extends RegistryClass> particles() { return null; }
    public Class<? extends RegistryClass> sounds() { return null; }
    public IModPackets packets() { return null; }
    public IModCapabilities capabilities() { return null; }
    public Config config() throws IOException, IllegalAccessException { return null; }

}
