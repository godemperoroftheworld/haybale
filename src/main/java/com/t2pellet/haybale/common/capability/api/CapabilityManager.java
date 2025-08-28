package com.t2pellet.haybale.common.capability.api;

import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

import java.util.List;

public interface CapabilityManager {

    /**
     * @return new CapabilityManager instance
     */
    static <E extends Entity & ICapabilityHaver> CapabilityManager newInstance(E entity) {
        return new CapabilityManagerImpl<>(entity);
    }

    /**
     * Add a new capability
     *
     * @param capabilityClass the class of the capablity to instantiate and add
     */
    <T extends Capability> T addCapability(Class<T> capabilityClass);

    /**
     * Get the capability instance for the given class
     *
     * @param capabilityClass the desired capability class
     * @return the instance for that class in this CapabilityManager
     */
    <T extends Capability> T getCapability(Class<T> capabilityClass);

    <T extends Capability> List<T> getCapabilities();

    /**
     * Update the capability instance for the given class
     *
     * @param capabilityClass the desired capability class
     * @param capability      the new instance for that class to set
     */
    <T extends Capability> void setCapability(Class<T> capabilityClass, T capability);

    /**
     * Write all capabilities in the manager to NBT
     * Call in Entity::addAdditionalSaveData
     *
     * @return the tag with all the capability data
     */
    Tag writeTag();

    /**
     * Read all capabilities in the NBT to the CapabilityManager
     * Call in Entity::readAdditionalSaveData
     *
     * @param tag the tag with all the capability data
     */
    void readTag(Tag tag);
}
