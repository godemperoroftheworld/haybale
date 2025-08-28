package com.t2pellet.haybale.common.mixin;

import com.t2pellet.haybale.common.capability.api.AbstractCapability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "addPairing", at = @At("TAIL"))
    public void addPairing(ServerPlayer player, CallbackInfo ci) {
        if (entity instanceof ICapabilityHaver capabilityHaver) {
            capabilityHaver.getCapabilityManager().getCapabilities().forEach(c -> {
                if (c instanceof AbstractCapability<?> abstractCapability) {
                    abstractCapability.synchronizeTo(player);
                }
            });
        }
    }

}
