package com.t2pellet.haybale.network.capability;

import com.t2pellet.haybale.entity.capability.api.Capability;
import com.t2pellet.haybale.entity.capability.api.ICapabilityHaver;
import com.t2pellet.haybale.network.api.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;

public class CapabilityPacket<E extends Entity & ICapabilityHaver> extends Packet {

    private E capabilityHaver;
    private final Class<? extends Capability> clazz;

    public CapabilityPacket(E capabilityHaver, Class<? extends Capability> clazz) {
        super();
        this.capabilityHaver = capabilityHaver;
        this.clazz = (Class<? extends Capability>) clazz.getInterfaces()[0];
    }

    public CapabilityPacket(FriendlyByteBuf byteBuf) throws ClassNotFoundException {
        super(byteBuf);
        String classStr = tag.getString("class");
        this.clazz = (Class<? extends Capability>) Class.forName(classStr);
    }

    @Override
    public Runnable getExecutor() {
        return () -> {
            Tag data = tag.get("data");
            int id = tag.getInt("entity");
            this.capabilityHaver = (E) Minecraft.getInstance().level.getEntity(id);
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
