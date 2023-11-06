package com.t2pellet.tlib;

import com.t2pellet.tlib.config.ExampleConfig;
import com.t2pellet.tlib.config.api.Config;
import com.t2pellet.tlib.network.TLibPackets;
import com.t2pellet.tlib.network.api.registry.IModPackets;
import com.t2pellet.tlib.registry.TlibEntities;
import com.t2pellet.tlib.registry.TlibParticles;
import com.t2pellet.tlib.registry.api.RegistryClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@TLibMod.IMod(TenzinLib.MODID)
public class TenzinLib extends TLibMod {

    public static final String MODID = "tlib";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final TenzinLib INSTANCE = new TenzinLib();

    private TenzinLib() {
    }

    @Override
    public Config config() throws IOException, IllegalAccessException {
        return new ExampleConfig();
    }

    @Override
    public IModPackets packets() {
        return new TLibPackets();
    }

    @Override
    public Class<? extends RegistryClass> particles() {
        return TlibParticles.class;
    }

    @Override
    public Class<? extends RegistryClass> entities() {
        return TlibEntities.class;
    }
}
