package com.t2pellet.haybale.common.network.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public abstract class Packet {

    protected CompoundTag tag;

    public Packet(FriendlyByteBuf byteBuf) {
        this.tag = byteBuf.readNbt();
    }

    public Packet() {
        tag = new CompoundTag();
    }

    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeNbt(tag);
    }

    public abstract Runnable getExecutor();


}
