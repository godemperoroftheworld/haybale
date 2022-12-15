package com.t2pellet.tlib.config;

public interface ConfigRegistrar {

    ConfigRegistrar INSTANCE = new ConfigRegistrar() {
        @Override
        public void register(String modid, Config config) {
        }

        @Override
        public Config get(String modid) {
            return null;
        }
    };

    void register(String modid, Config config);

    Config get(String modid);

}
