package com.t2pellet.haybale.common.capability.registry;

import com.t2pellet.haybale.common.capability.api.AbstractCapability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import com.t2pellet.haybale.common.utils.VersionHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

public class ExampleCapabilityImpl<E extends Entity & ICapabilityHaver> extends AbstractCapability<E> implements ExampleCapability {

    private int lifeTicks = 20;

    protected ExampleCapabilityImpl(E entity) {
        super(entity);
    }

    @Override
    public int count() {
        return lifeTicks;
    }

    public void decrement() {
        lifeTicks -= 1;
        synchronize();
    }

    @Override
    public Tag writeTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("lifeTicks", lifeTicks);
        return tag;
    }

    @Override
    public void readTag(Tag tag) {
        CompoundTag compoundTag = (CompoundTag) tag;
        //? if <= 1.21.4 {
        lifeTicks = compoundTag.getInt("lifeTicks");
        //?} else {
        /*lifeTicks = compoundTag.getInt("lifeTicks").orElseThrow();
        *///?}
    }
}
