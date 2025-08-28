//? if neoforge {
package com.t2pellet.haybale.neoforge;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.client.HaybaleClient;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.neoforge.network.PacketHandler;
import com.t2pellet.haybale.neoforge.services.SidedExecutor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mod(Haybale.MODID)
@HaybaleMod.IMod(Haybale.MODID)
public class HaybaleNeoforge extends HaybaleNeoforgeMod {

    private static HaybaleNeoforge instance = null;
    public static HaybaleNeoforge getInstance() {
        return instance;
    }

    private Map<String, HaybaleNeoforgeMod> modMap;

    @Override
    protected void initialSetup() {
        instance = this;
        modMap = new HashMap<>();
    }

    @Override
    protected HaybaleMod getCommonMod() {
        return Haybale.INSTANCE;
    }

    @Override
    protected HaybaleModClient getClientMod() {
        return HaybaleClient.INSTANCE;
    }

    @Override
    protected void registerEvents(IEventBus bus) {
        // Mod events
        bus.addListener(((PacketHandler) Services.PACKET_HANDLER)::registerPackets);
        // Other events
        NeoForge.EVENT_BUS.addListener(((SidedExecutor) Services.SIDE)::onServerTick);
    }

    public void register(String id, HaybaleNeoforgeMod mod) {
        modMap.put(id, mod);
    }

    public HaybaleNeoforgeMod get(String modid) {
        return modMap.get(modid);
    }
}
//?}