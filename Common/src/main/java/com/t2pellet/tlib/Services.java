package com.t2pellet.tlib;

import com.t2pellet.tlib.client.network.IClientPacketHandler;
import com.t2pellet.tlib.client.registry.IClientRegistry;
import com.t2pellet.tlib.common.network.ICommonPacketHandler;
import com.t2pellet.tlib.common.registry.ICommonRegistry;

import java.util.ServiceLoader;

public class Services {

    static final ICommonRegistry COMMON_REGISTRY = load(ICommonRegistry.class);
    static final IClientRegistry CLIENT_REGISTRY = load(IClientRegistry.class);

    static final ICommonPacketHandler COMMON_PACKETS = load(ICommonPacketHandler.class);
    static final IClientPacketHandler CLIENT_PACKETS = load(IClientPacketHandler.class);

    private static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        TenzinLib.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}
