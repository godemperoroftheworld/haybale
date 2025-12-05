package com.t2pellet.haybale.common.entity;

import com.t2pellet.haybale.common.capability.api.CapabilityManager;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import com.t2pellet.haybale.common.capability.registry.ExampleCapability;
import com.t2pellet.haybale.common.utils.VersionHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

//? if >= 1.21.2 {
/*import net.minecraft.server.level.ServerLevel;
*///?}

public class ExampleEntity extends Cow implements ICapabilityHaver {

    CapabilityManager capabilities = CapabilityManager.newInstance(this);
    private final ExampleCapability example;

    public ExampleEntity(EntityType<? extends Cow> arg, Level arg2) {
        super(arg, arg2);
        example = capabilities.addCapability(ExampleCapability.class);
    }

    @Override
    public CapabilityManager getCapabilityManager() {
        return capabilities;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (VersionHelper.getLevel(this).isClientSide) return;
        if (random.nextInt(10) == 0) {
            example.decrement();
        }
        if (example.count() == 0) {
            //? if < 1.21.2 {
            kill();
            //?} else {
            /*Level level = VersionHelper.getLevel(this);
            if (level instanceof ServerLevel serverLevel) {
                kill(serverLevel);
            }
            *///?}
        }
    }

    @Override
    public @NotNull Component getName() {
        String literal = String.valueOf(example.count());
        return VersionHelper.literalComponent(literal);
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }
}
