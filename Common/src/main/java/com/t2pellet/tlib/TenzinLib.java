package com.t2pellet.tlib;

import com.t2pellet.tlib.client.TLibModClient;
import com.t2pellet.tlib.common.TLibMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class TenzinLib {

    public static final TenzinLib INSTANCE = new TenzinLib();
    public static final String MODID = "tlib";
    public static final Logger LOG = LogManager.getLogger(MODID);

    private TenzinLib() {
    }
    
}
