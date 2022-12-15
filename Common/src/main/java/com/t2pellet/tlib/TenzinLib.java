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

    private final Map<String, TLibMod> modMap = new HashMap<>();
    private final Map<String, TLibModClient> clientModMap = new HashMap<>();

    private TenzinLib() {
    }

    public void register(String id, TLibMod common) {
        modMap.put(id, common);
    }

    public void register(String id, TLibModClient client) {
        clientModMap.put(id, client);
    }

    public TLibMod get(String id) {
        return modMap.get(id);
    }

    public TLibModClient getClient(String id) {
        return clientModMap.get(id);
    }
    
}
