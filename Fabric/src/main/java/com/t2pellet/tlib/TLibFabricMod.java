package com.t2pellet.tlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface TLibFabricMod extends ModInitializer, ClientModInitializer {


    @Override
    default void onInitializeClient() {

    }

    @Override
    default void onInitialize() {

    }
}
