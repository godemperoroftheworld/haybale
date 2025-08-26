package com.t2pellet.haybale;

import com.t2pellet.haybale.entity.capability.api.registry.IModCapabilities;
import com.t2pellet.haybale.network.api.registry.IModPackets;
import com.t2pellet.haybale.config.api.Config;
import com.t2pellet.haybale.registry.api.RegistryClass;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public abstract class HaybaleMod {

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
