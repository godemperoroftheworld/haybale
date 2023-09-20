package com.t2pellet.tlib.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public interface ConfigRegistrar {

    ConfigRegistrar INSTANCE = new ConfigRegistrarImpl();

    <T extends Config> void register(String modid, Supplier<T> configSupplier);

    <T extends Config> T get(String modid);

    Set<String> getAllRegistered();

}
