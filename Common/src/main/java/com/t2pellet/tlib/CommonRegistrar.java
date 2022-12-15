package com.t2pellet.tlib;

import com.t2pellet.tlib.common.registry.IModEntities;
import com.t2pellet.tlib.common.registry.IModItems;
import com.t2pellet.tlib.common.registry.IModParticles;
import com.t2pellet.tlib.common.registry.IModSounds;
import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CommonRegistrar {


    public static void register(String modid, IModEntities entities) {
        for (Field field : entities.getClass().getDeclaredFields()) {
            IModEntities.IEntity entityInfo = field.getDeclaredAnnotation(IModEntities.IEntity.class);
            if (entityInfo != null && field.getType().equals(IModEntities.TLibEntity.class)) {
                try {
                    IModEntities.TLibEntity<? extends LivingEntity> entity = (IModEntities.TLibEntity<? extends LivingEntity>) field.get(null);
                    Field result = entity.getClass().getDeclaredField("TYPE");
                    setField(result, entity, Services.COMMON_REGISTRY.registerEntity(modid, entityInfo.name(), entity._factory, entityInfo.category(), entityInfo.width(), entityInfo.height(), entity._attributes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register(String modid, IModItems items) {
        for (Field field : items.getClass().getDeclaredFields()) {
            IModItems.IItem itemInfo = field.getDeclaredAnnotation(IModItems.IItem.class);
            if (itemInfo != null && field.getType().equals(IModItems.TLibItem.class)) {
                try {
                    IModItems.TLibItem item = (IModItems.TLibItem) field.get(null);
                    Field result = item.getClass().getDeclaredField("ITEM");
                    setField(result, item, Services.COMMON_REGISTRY.registerItem(modid, itemInfo.name(), item._properties));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register(String modid, IModParticles particles) {
        for (Field field : particles.getClass().getDeclaredFields()) {
            IModParticles.IParticle particleInfo = field.getAnnotation(IModParticles.IParticle.class);
            if (particleInfo != null && field.getType().equals(IModParticles.TLibParticle.class)) {
                try {
                    IModParticles.TLibParticle particle = (IModParticles.TLibParticle) field.get(null);
                    Field result = particle.getClass().getDeclaredField("PARTICLE");
                    setField(result, particle, Services.COMMON_REGISTRY.registerParticle(modid, particleInfo.value()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void register(String modid, IModSounds sounds) {
        for (Field field : sounds.getClass().getDeclaredFields()) {
            IModSounds.ISound soundInfo = field.getAnnotation(IModSounds.ISound.class);
            if (soundInfo != null && field.getType().equals(IModSounds.TLibSound.class)) {
                try {
                    IModSounds.TLibSound sound = (IModSounds.TLibSound) field.get(null);
                    Field result = sound.getClass().getDeclaredField("SOUND");
                    setField(result, sound, Services.COMMON_REGISTRY.registerSound(modid, soundInfo.value()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private static void setField(Field field, Object object, Object value) {
        try {
            field.setAccessible(true);
            field.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
