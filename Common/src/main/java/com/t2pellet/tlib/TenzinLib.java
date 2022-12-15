package com.t2pellet.tlib;

import com.mojang.datafixers.util.Pair;
import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.TLibMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TenzinLib {

    public static final TenzinLib INSTANCE = new TenzinLib();
    static final String MODID = "tlib";
    static final Logger LOG = LogManager.getLogger(MODID);

    private Map<String, Pair<TLibMod, TLibModClient>> modMap = new HashMap<>();

    private TenzinLib() {
    }

    public void register(String id, TLibMod common, TLibModClient client) {
        modMap.put(id, Pair.of(common, client));
    }

    void preInit() {
        LOG.info("Pre-init started");
        for (Map.Entry<String, Pair<TLibMod, TLibModClient>> entry : modMap.entrySet()) {
            LOG.info("Pre-initializing: " + entry.getKey());
            String modid = entry.getKey();
            TLibMod common = entry.getValue().getFirst();
            CommonRegistrar.register(modid, common.items());
            CommonRegistrar.register(modid, common.entities());
            CommonRegistrar.register(modid, common.particles());
            CommonRegistrar.register(modid, common.sounds());
            LOG.info("Pre-initialized: " + entry.getKey());
        }
        LOG.info("Pre-init finished");
    }

    void clientInit() {
        LOG.info("Client init started");
        for (Map.Entry<String, Pair<TLibMod, TLibModClient>> entry : modMap.entrySet()) {
            LOG.info("Client-initializing: " + entry.getKey());
            String modid = entry.getKey();
            TLibModClient client = entry.getValue().getSecond();
            ClientRegistrar.register(modid, client.entityModels());
            ClientRegistrar.register(modid, client.particleFactories());
            LOG.info("Client-initialized: " + entry.getKey());
        }
    }

    void init() {
        LOG.info("Init started");
        for (Map.Entry<String, Pair<TLibMod, TLibModClient>> entry : modMap.entrySet()) {
            LOG.info("Initializing " + entry.getKey());
            TLibMod client = entry.getValue().getFirst();
            client.registerPackets(PacketRegistrar.INSTANCE);
            LOG.info("Initialized: " + entry.getKey());
        }
        LOG.info("Init finished");
    }
    
}
