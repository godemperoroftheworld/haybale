package com.t2pellet.tlib.mixin;

import com.t2pellet.tlib.entity.capability.api.AbstractCapability;
import com.t2pellet.tlib.entity.capability.api.ICapabilityHaver;
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
            System.out.println("Capability haver");
            capabilityHaver.getCapabilityManager().getCapabilities().forEach(c -> {
                System.out.println("Synchronizing capability: " + c.getClass().getName());
                if (c instanceof AbstractCapability<?> abstractCapability) {
                    abstractCapability.synchronizeTo(player);
                    System.out.println("Synchronized capability: " + abstractCapability.getClass().getName());
                }
            });
        }
    }

}
