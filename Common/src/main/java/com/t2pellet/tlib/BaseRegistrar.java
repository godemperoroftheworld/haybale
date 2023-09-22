package com.t2pellet.tlib;

import com.t2pellet.tlib.registry.api.EntryType;
import com.t2pellet.tlib.registry.api.RegistryClass;

import java.lang.reflect.Field;

public abstract class BaseRegistrar {

    BaseRegistrar() {}

    protected void registerFromClass(String modid, Class<? extends RegistryClass> registerClass) {
        // Need IRegistryClass to do some type checking
        if (registerClass == null) return;
        if (registerClass.isAnnotationPresent(RegistryClass.IRegistryClass.class)) {
            RegistryClass.IRegistryClass registryClassInfo = registerClass.getAnnotation(RegistryClass.IRegistryClass.class);
            Class<?> registryType = registryClassInfo.value();
            // Only some entry types are supported
            if (checkIsValid(registryType)) {
                // Try and register all declared fields
                for (Field declaredField : registerClass.getDeclaredFields()) {
                    Class<?> fieldType = declaredField.getType();
                    // Fields need a ICommonEntry annotation, ignored otherwise. Must be child of EntryType.
                    if (declaredField.isAnnotationPresent(RegistryClass.IRegistryEntry.class) && EntryType.class.isAssignableFrom(fieldType)) {
                        try {
                            doGenericRegistration(modid, registryType, declaredField);
                        } catch (IllegalAccessException ex) {
                            TenzinLib.LOG.error("Failed registration for " + modid + "for type: " + registryType.getSimpleName());
                            TenzinLib.LOG.error(ex);
                        }
                    }
                }
            } else TenzinLib.LOG.error("Invalid registry type: " + registryType.getName());
        } else TenzinLib.LOG.debug("Skipping registration class " + registerClass.getName() + " for " + modid + ". No IRegistryClass annotation found.");
    }

    protected abstract boolean checkIsValid(Class<?> registryType);

    protected abstract void doGenericRegistration(String modid, Class<?> registryType, Field declaredField) throws IllegalAccessException;

    protected void setField(String name, Object object, Object value) {
        try {
            Field field = object.getClass().getSuperclass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            TenzinLib.LOG.error("Reflection setField failed");
            TenzinLib.LOG.error(e);
        }
    }
}
