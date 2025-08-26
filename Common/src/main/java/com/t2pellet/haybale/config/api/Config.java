package com.t2pellet.haybale.config.api;

import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.config.api.property.*;
import org.ini4j.Ini;
import org.ini4j.Profile;
import org.jetbrains.annotations.NotNull;

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
    private final Ini ini;

    protected Config(String modid) throws IOException, IllegalAccessException {
        this.file = new File(CONFIG_DIR + modid + ".ini");
        if (!file.exists()) file.createNewFile();
        this.ini = new Ini(file);
    }

    /**
     * Save the Config to disk
     *
     * @throws IOException            if there is an error writing to disk
     * @throws IllegalAccessException if there is an error accessing config elements
     */
    public void save() throws IOException, IllegalAccessException {
        if (!ini.getFile().exists()) ini.getFile().createNewFile();
        ini.clear();
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
                            Object value = property.get();
                            section.add(declaredField.getName(), value);
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
     * @throws IllegalAccessException if there is an error loading the config
     */
    public void load() throws IllegalAccessException {
        // Skip load for first time
        if (ini.isEmpty() || ini.getFile().length() == 0) {
            return;
        }
        Class<?>[] declaredClasses = this.getClass().getDeclaredClasses();
        for (Class<?> declaredClass : declaredClasses) {
            if (declaredClass.isAnnotationPresent(Section.class)) {
                Section sectionAnnotation = declaredClass.getAnnotation(Section.class);
                Profile.Section section = ini.get(sectionAnnotation.name());
                if (section != null) loadSection(declaredClass, section);
            }
        }
    }

    private void loadSection(Class<?> sectionClass, @NotNull Profile.Section section) throws IllegalAccessException {
        for (Field declaredField : sectionClass.getDeclaredFields()) {
            Object fieldValue = declaredField.get(null);
            if (fieldValue instanceof ConfigProperty<?> property) {
                if (!section.containsKey(declaredField.getName())) continue;
                if (property instanceof IntProperty intProperty) {
                    int value = section.get(declaredField.getName(), Integer.class);
                    intProperty.set(value);
                } else if (property instanceof FloatProperty floatProperty) {
                    float value = section.get(declaredField.getName(), Float.class);
                    floatProperty.set(value);
                } else if (property instanceof BoolProperty boolProperty) {
                    boolean value = section.get(declaredField.getName(), Boolean.class);
                    boolProperty.set(value);
                } else if (property instanceof StringProperty stringProperty) {
                    String value = section.get(declaredField.getName());
                    stringProperty.set(value);
                } else if (property instanceof ListProperty<?> listProperty) {
                    String strValue = section.get(declaredField.getName(), String.class);
                    listProperty.setValue(strValue);
                }
            }
        }
    }
}
