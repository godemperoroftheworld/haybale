package com.t2pellet.tlib.config;

import com.t2pellet.tlib.TenzinLib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class ConfigRegistrarImpl implements ConfigRegistrar {

    private final Map<String, Config> configMap;

    ConfigRegistrarImpl() {
        configMap = new HashMap<>();
    }


    @Override
    public <T extends Config> void register(String modid, Supplier<T> configSupplier) {
        T config = configSupplier.get();
        try {
            config.load();
        } catch (Exception e) {
            TenzinLib.LOG.error("Failed to register config for mod: " + modid);
            TenzinLib.LOG.error(e.toString());
        }
        configMap.put(modid, config);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Config> T get(String modid) {
        return (T) configMap.get(modid);
    }
}