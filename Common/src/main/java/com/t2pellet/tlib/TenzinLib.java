package com.t2pellet.tlib;

import com.t2pellet.tlib.common.TLibMod;
import com.t2pellet.tlib.common.network.IModPackets;
import com.t2pellet.tlib.common.network.TLibPackets;
import com.t2pellet.tlib.config.Config;
import com.t2pellet.tlib.config.ExampleConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@TLibMod.IMod(TenzinLib.MODID)
public class TenzinLib extends TLibMod {

    public static final String MODID = "tlib";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final TenzinLib INSTANCE = new TenzinLib();

    private TenzinLib() {
    }

    @Override
    public Config config() {
        return ExampleConfig.INSTANCE;
    }

    @Override
    public IModPackets packets() {
        return new TLibPackets();
    }
}
