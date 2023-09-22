package com.t2pellet.tlib.mixin;

import com.t2pellet.tlib.entity.capability.api.ICapabilityHaver;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class EntityMixin {

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readFromTag(CompoundTag tag, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver && tag.contains("capabilities")) {
            capabilityHaver.getCapabilityManager().readTag(tag.get("capabilities"));
            System.out.println("Read capabilities: " + tag.get("capabilities"));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void writeToTag(CompoundTag tag, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver) {
            Tag capabilityTag = capabilityHaver.getCapabilityManager().writeTag();
            tag.put("capabilities", capabilityTag);
            System.out.println("Wrote capabilities: " + capabilityTag);
        }
    }

}
