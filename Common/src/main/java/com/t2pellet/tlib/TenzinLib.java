package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.TLibMod;
import com.t2pellet.tlib.common.network.IModPackets;
import com.t2pellet.tlib.common.network.TLibPackets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@TLibMod.IMod(TenzinLib.MODID)
public class TenzinLib extends TLibMod {

    public static final String MODID = "tlib";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final TenzinLib INSTANCE = new TenzinLib();

    private TenzinLib() {
    }

    @Override
    public IModPackets packets() {
        return new TLibPackets();
    }
}
