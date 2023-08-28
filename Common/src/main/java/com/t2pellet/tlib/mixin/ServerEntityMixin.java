package com.t2pellet.tlib.mixin;

import com.t2pellet.tlib.common.entity.capability.AbstractCapability;
import com.t2pellet.tlib.common.entity.capability.ICapabilityHaver;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Inject(method = "addPairing", at = @At("TAIL"))
    public void addPairing(ServerPlayer player, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver) {
            capabilityHaver.getCapabilityManager().getCapabilities().forEach(c -> {
                if (c instanceof AbstractCapability<?> abstractCapability) {
                    abstractCapability.synchronizeTo(player);
                }
            });
        }
    }

}
