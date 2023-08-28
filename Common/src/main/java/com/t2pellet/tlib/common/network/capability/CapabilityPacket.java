package com.t2pellet.tlib.common.network.capability;

import com.t2pellet.tlib.common.entity.capability.Capability;
import com.t2pellet.tlib.common.entity.capability.ICapabilityHaver;
import com.t2pellet.tlib.common.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.entity.EntityAccess;

public class CapabilityPacket<E extends ICapabilityHaver & EntityAccess> extends Packet {

    private final E capabilityHaver;
    private final Class<? extends Capability> clazz;

    public CapabilityPacket(E capabilityHaver, Class<? extends Capability> clazz) {
        super();
        this.capabilityHaver = capabilityHaver;
        this.clazz = clazz;
    }

    public CapabilityPacket(FriendlyByteBuf byteBuf) throws ClassNotFoundException {
        super(byteBuf);
        int id = tag.getInt("entity");
        String classStr = tag.getString("class");
        this.capabilityHaver = (E) Minecraft.getInstance().level.getEntity(id);
        this.clazz = (Class<? extends Capability>) Class.forName(classStr);
    }

    @Override
    public Runnable getExecutor() {
        return () -> {
            CompoundTag data = tag.getCompound("data");
            capabilityHaver.getCapabilityManager().getCapability(clazz).readTag(data);
        };
    }

    @Override
    public void encode(FriendlyByteBuf byteBuf) {
        Capability capability = capabilityHaver.getCapabilityManager().getCapability(clazz);
        tag.putInt("entity", capabilityHaver.getId());
        tag.putString("class", clazz.getName());
        tag.put("data", capability.writeTag());

        super.encode(byteBuf);
    }
}
