package com.t2pellet.haybale.common.config;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.common.config.api.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class ConfigRegistrarImpl implements ConfigRegistrar {

    private final Map<String, Config> configMap;

    ConfigRegistrarImpl() {
        configMap = new HashMap<>();
    }


    @Override
    public <T extends Config> void register(String modid, ConfigSupplier<T> configSupplier) {
        try {
            T config = configSupplier.get();
            config.load();
            config.save();
            configMap.put(modid, config);
        } catch (Exception e) {
            Haybale.LOG.error("Failed to register config for mod: " + modid);
            Haybale.LOG.error(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Config> T get(String modid) {
        return (T) configMap.get(modid);
    }

    @Override
    public Set<String> getAllRegistered() {
        return configMap.keySet();
    }
}