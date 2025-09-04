package com.t2pellet.haybale.mixin;

import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if > 1.21.5 {
/*import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import java.util.Optional;
import com.google.gson.JsonElement;
import com.t2pellet.haybale.common.utils.compound.JsonToNbt;
import com.t2pellet.haybale.common.utils.compound.NbtToJson;
import net.minecraft.util.ExtraCodecs;
*///?} else {
import net.minecraft.nbt.CompoundTag;
//?}

@Mixin(LivingEntity.class)
public class EntityMixin {

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    //? if > 1.21.5 {
    /*public void readFromTag(ValueInput valueInput, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver) {
            Optional<JsonElement> json = valueInput.read("capabilityData", ExtraCodecs.JSON);
            if (json.isPresent()) {
                Tag tag = JsonToNbt.toNbt(json.get());
                capabilityHaver.getCapabilityManager().readTag(tag);
            }
        }
    }
    *///?} else {
    public void readFromTag(CompoundTag tag, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver && tag.contains("capabilities")) {
            capabilityHaver.getCapabilityManager().readTag(tag.get("capabilities"));
        }
    }
    //?}

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    //? if > 1.21.5 {
    /*public void writeToTag(ValueOutput valueOutput, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver) {
            Tag capabilityTag = capabilityHaver.getCapabilityManager().writeTag();
            JsonElement json = NbtToJson.toJson(capabilityTag);
            valueOutput.store("capabilityData", ExtraCodecs.JSON, json);
        }
    }
    *///?} else {
    public void writeToTag(CompoundTag tag, CallbackInfo ci) {
        Entity e = (Entity) (Object) this;
        if (e instanceof ICapabilityHaver capabilityHaver) {
            Tag capabilityTag = capabilityHaver.getCapabilityManager().writeTag();
            tag.put("capabilities", capabilityTag);
        }
    }
    //?}

}
