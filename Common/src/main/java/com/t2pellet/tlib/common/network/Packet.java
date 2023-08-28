package com.t2pellet.tlib.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
