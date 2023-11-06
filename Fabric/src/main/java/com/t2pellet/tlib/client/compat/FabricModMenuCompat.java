package com.t2pellet.tlib.client.compat;

import com.t2pellet.tlib.Services;
import com.t2pellet.tlib.TenzinLib;
import com.t2pellet.tlib.config.ConfigRegistrar;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import java.util.HashMap;
import java.util.Map;

public class FabricModMenuCompat implements ModMenuApi {

    private final Map<String, ConfigScreenFactory<?>> map;
    private final ConfigMenu self = new ConfigMenu(TenzinLib.MODID);

    public FabricModMenuCompat() {
        map = new HashMap<>();
        if (!Services.PLATFORM.isModLoaded("cloth-config")) {
            TenzinLib.LOG.debug("No cloth config");
            return;
        }
        for (String modid : ConfigRegistrar.INSTANCE.getAllRegistered()) {
            ConfigMenu menu = new ConfigMenu(modid);
            map.put(modid, screen -> menu.buildConfigScreen());
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> self.buildConfigScreen();
    }

    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        return map;
    }
}
