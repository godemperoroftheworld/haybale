package com.t2pellet.haybale;

import com.t2pellet.haybale.client.registry.IClientRegistry;
import com.t2pellet.haybale.network.IPacketHandler;
import com.t2pellet.haybale.registry.ICommonRegistry;
import com.t2pellet.haybale.services.IPlatformHelper;
import com.t2pellet.haybale.services.IServerHelper;
import com.t2pellet.haybale.services.ISidedExecutor;

import java.util.ServiceLoader;

public class Services {

    public static final ISidedExecutor SIDE = load(ISidedExecutor.class);
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IServerHelper SERVER_HELPER = load(IServerHelper.class);
    public static final IPacketHandler PACKET_HANDLER = load(IPacketHandler.class);
    static final ICommonRegistry COMMON_REGISTRY = load(ICommonRegistry.class);
    static final IClientRegistry CLIENT_REGISTRY = load(IClientRegistry.class);


    private static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Haybale.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}
