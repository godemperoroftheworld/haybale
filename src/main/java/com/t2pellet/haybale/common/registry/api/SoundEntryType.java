package com.t2pellet.haybale.common.registry.api;

import net.minecraft.sounds.SoundEvent;

public class SoundEntryType extends EntryType<SoundEvent>  {

    private final String name;

    public SoundEntryType(String name) {
        super(SoundEvent.class);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
