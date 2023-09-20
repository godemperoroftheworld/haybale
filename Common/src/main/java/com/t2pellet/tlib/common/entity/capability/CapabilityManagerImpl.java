package com.t2pellet.tlib.common.entity.capability;

import com.t2pellet.tlib.TenzinLib;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapabilityManagerImpl<E extends Entity & ICapabilityHaver > implements CapabilityManager {


    private final Map<Class<? extends Capability>, Capability> map = new HashMap<>();
    private final E entity;

    CapabilityManagerImpl(E entity) {
        this.entity = entity;
    }

    @Override
    public <T extends Capability> T addCapability(Class<T> capabilityClass) {
        instantiateCapability(capabilityClass);
        return (T) map.get(capabilityClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Capability> T getCapability(Class<T> capabilityClass) {
        return (T) map.get(capabilityClass);
    }

    @Override
    public List<Capability> getCapabilities() {
        return map.values().stream().toList();
    }

    @Override
    public <T extends Capability> void setCapability(Class<T> capabilityClass, T capability) {
        map.put(capabilityClass, capability);
    }

    @Override
    public Tag writeTag() {
        ListTag tag = new ListTag();
        map.forEach((aClass, capability) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("className", aClass.getName());
            compoundTag.put("capability", capability.writeTag());
            tag.add(compoundTag);
        });
        return tag;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void readTag(Tag tag) {
        ListTag listTag = (ListTag) tag;
        listTag.forEach(tagInList -> {
            CompoundTag compoundTag = (CompoundTag) tagInList;
            try {
                Class<? extends Capability> aClass = (Class<? extends Capability>) Class.forName(compoundTag.getString("className"));
                if (!map.containsKey(aClass)) instantiateCapability(aClass);
                map.get(aClass).readTag(compoundTag.get("capability"));
            } catch (ClassNotFoundException e) {
                TenzinLib.LOG.error("Failed to instantiate capability from NBT", e);
            }
        });
    }

    private <T extends Capability> void instantiateCapability(Class<T> aClass) {
        map.put(aClass, CapabilityRegistrar.INSTANCE.get(aClass, entity).orElseThrow(() -> new InstantiationError("Failed to instantiate capability for class: " + aClass.getSimpleName())));
    }
}
