package com.t2pellet.haybale.common.capability.api;

import net.minecraft.nbt.Tag;

public interface Capability {

    /**
     * Writes the capability data to a tag
     *
     * @return the data tag
     */
    Tag writeTag();

    /**
     * Reads the capability data from a tag
     *
     * @param tag the data tag
     */
    void readTag(Tag tag);
}
