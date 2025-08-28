package com.t2pellet.haybale;

import com.t2pellet.haybale.common.config.ExampleConfig;
import com.t2pellet.haybale.common.config.api.Config;
import com.t2pellet.haybale.common.network.HaybalePackets;
import com.t2pellet.haybale.common.network.api.registry.IModPackets;
import com.t2pellet.haybale.common.registry.HaybaleEntities;
import com.t2pellet.haybale.common.registry.HaybaleParticles;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@HaybaleMod.IMod(Haybale.MODID)
public class Haybale extends HaybaleMod {

    public static final String MODID = "haybale";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final Haybale INSTANCE = new Haybale();

    private Haybale() {
    }

    @Override
    public Config config() throws IOException, IllegalAccessException {
        return new ExampleConfig();
    }

    @Override
    public IModPackets packets() {
        return new HaybalePackets();
    }

    @Override
    public Class<? extends RegistryClass> particles() {
        return HaybaleParticles.class;
    }

    @Override
    public Class<? extends RegistryClass> entities() {
        return HaybaleEntities.class;
    }
}
