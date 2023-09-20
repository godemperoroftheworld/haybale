package com.t2pellet.tlib.config;

import com.t2pellet.tlib.Services;
import com.t2pellet.tlib.TenzinLib;
import com.t2pellet.tlib.config.property.*;
import org.ini4j.Ini;
import org.ini4j.Profile;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class Config {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface ModConfig {
        String comment();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface Section {
        String name();
        String description();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Entry {
        String comment();
    }

    private static final String CONFIG_DIR = Services.PLATFORM.getGameDir() + "/config/";

    private final File file;

    protected Config(String modid) {
        this.file = new File(CONFIG_DIR + modid + ".ini");
        try {
            if (!file.exists()) file.createNewFile();
            if (file.length() == 0) save();
        } catch (Exception ex) {
            TenzinLib.LOG.error("Failed to create config file");
            TenzinLib.LOG.error(ex.getStackTrace());
        }
    }

    /**
     * Save the Config to disk
     *
     * @throws IOException            if there is an error writing to disk
     * @throws IllegalAccessException if there is an error accessing config elements
     */
    public void save() throws IOException, IllegalAccessException {
        Ini ini = new Ini(file);
        if (this.getClass().isAnnotationPresent(ModConfig.class)) {
            ModConfig modConfig = this.getClass().getAnnotation(ModConfig.class);
            ini.setComment(modConfig.comment());
        }
        for (Class<?> aClass : this.getClass().getDeclaredClasses()) {
            if (aClass.isAnnotationPresent(Section.class)) {
                Section sectionAnnotation = aClass.getAnnotation(Section.class);
                ini.putComment(sectionAnnotation.name(), sectionAnnotation.description());
                Profile.Section section = ini.add(sectionAnnotation.name());
                for (Field declaredField : aClass.getDeclaredFields()) {
                    if (declaredField.isAnnotationPresent(Entry.class)) {
                        Entry entryAnnotation = declaredField.getAnnotation(Entry.class);
                        Object fieldValue = declaredField.get(null);
                        declaredField.setAccessible(true);
                        if (fieldValue instanceof ConfigProperty<?> property) {
                            Object value = property.getValue();
                            section.put(declaredField.getName(), value);
                            section.putComment(declaredField.getName(), entryAnnotation.comment());
                        }
                    }
                }
            }
        }
        ini.store();
    }

    /**
     * Loads the Config from disk
     *
     * @throws IOException            if there is an error reading from disk
     * @throws IllegalAccessException if there is an error accessing config elements
     */
    public void load() throws IOException, IllegalAccessException {
        Ini ini = new Ini(file);
        // Skip load for first time, set up file.
        if (ini.isEmpty()) {
            save();
            return;
        }
        Class<?>[] declaredClasses = this.getClass().getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses) {
            if (declaredClass.isAnnotationPresent(Section.class)) {
                Section section = declaredClass.getAnnotation(Section.class);
                for (Field declaredField : declaredClass.getDeclaredFields()) {
                    Object fieldValue = declaredField.get(null);
                    if (fieldValue instanceof ConfigProperty<?> property) {
                        if (property instanceof IntProperty intProperty) {
                            int value = ini.get(section.name(), declaredField.getName(), Integer.class);
                            intProperty.setValue(value);
                        } else if (property instanceof FloatProperty floatProperty) {
                            float value = ini.get(section.name(), declaredField.getName(), Float.class);
                            floatProperty.setValue(value);
                        } else if (property instanceof BoolProperty boolProperty) {
                            boolean value = ini.get(section.name(), declaredField.getName(), Boolean.class);
                            boolProperty.setValue(value);
                        } else if (property instanceof StringProperty stringProperty) {
                            String value = ini.get(section.name(), declaredField.getName());
                            stringProperty.setValue(value);
                        } else if (property instanceof ListProperty<?> listProperty) {
                            String strValue = ini.get(section.name(), declaredField.getName(), String.class);
                            listProperty.setValue(strValue);
                        }
                    }
                }
            }
        }
    }
}
