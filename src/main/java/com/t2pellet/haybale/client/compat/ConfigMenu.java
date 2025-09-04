package com.t2pellet.haybale.client.compat;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.Services;
import com.t2pellet.haybale.common.config.ConfigRegistrar;
import com.t2pellet.haybale.common.config.api.Config;
import com.t2pellet.haybale.common.config.api.property.*;
import com.t2pellet.haybale.common.utils.VersionHelper;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import java.io.IOException;
import java.lang.reflect.Field;

public class ConfigMenu {

    interface IBuilderFactory<T, R extends TooltipListEntry<T>, S extends FieldBuilder<T, R, S>> {
        FieldBuilder<T, R, S> create(Component component, T value);
    }

    final String modid;
    final Config config;

    // needs to be public for fabric ModMenu integration
    public ConfigMenu(String modid) {
        this.modid = modid;
        this.config = ConfigRegistrar.INSTANCE.get(this.modid);
    }

    public Screen buildConfigScreen() {
        try {
            return configBuilder().build();
        } catch (IllegalAccessException ex) {
            Haybale.LOG.error("Failed to build config screen for mod: " + modid);
            Haybale.LOG.error(ex);
        }
        return null;
    }

    private ConfigBuilder configBuilder() throws IllegalAccessException {
        Component component = VersionHelper.translatableComponent("title." + modid + ".config");
        ConfigBuilder builder = ConfigBuilder.create().setTitle(component);
        for (Class<?> clazz : config.getClass().getDeclaredClasses()) {
            Config.Section section = clazz.getAnnotation(Config.Section.class);
            if (section != null) {
                addSection(builder, clazz, section);
            }
        }
        return builder;
    }

    private void addSection(ConfigBuilder configBuilder, Class<?> clazz, Config.Section section) throws IllegalAccessException {
        Component component = VersionHelper.literalComponent(section.name());
        ConfigCategory category = configBuilder.getOrCreateCategory(component);
        category.setDescription(new FormattedText[]{FormattedText.of(section.description())});
        ConfigEntryBuilder builder = configBuilder.entryBuilder();
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> type = field.getType();
            field.setAccessible(true);
            if (StringProperty.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startStrField, field));
            } else if (IntProperty.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startIntField, field));
            } else if (BoolProperty.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startBooleanToggle, field));
            } else if (FloatProperty.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startFloatField, field));
            } else if (ListProperty.class.isAssignableFrom(type)) {
                ListProperty<?> property = (ListProperty<?>) field.get(null);
                if (property.getType() == PropertyType.INT) {
                    category.addEntry(addField(builder::startIntList, field));
                } else if (property.getType() == PropertyType.FLOAT) {
                    category.addEntry(addField(builder::startFloatList, field));
                } else category.addEntry(addField(builder::startStrList, field));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T, R extends TooltipListEntry<T>, S extends FieldBuilder<T, R, S>> R addField(IBuilderFactory<T, R, S> builderFactory, Field field) throws IllegalAccessException {
        ConfigProperty<T> property = (ConfigProperty<T>) field.get(null);
        T value = property.get();
        Config.Entry comment = field.getAnnotation(Config.Entry.class);
        Component component = VersionHelper.literalComponent(field.getName());
        FieldBuilder<T, R, S> fieldBuilder = builderFactory.create(component, value);
        if (fieldBuilder instanceof AbstractFieldBuilder<T,R,S> betterFieldBuilder) {
            betterFieldBuilder.setDefaultValue(property.getDefault());
            betterFieldBuilder.setSaveConsumer(s -> updateProperty(property, s));
            Component tooltip = VersionHelper.literalComponent(comment.comment());
            if (comment != null) betterFieldBuilder.setTooltip(tooltip);
        }
        return fieldBuilder.build();
    }

    private <T> void updateProperty(ConfigProperty<T> property, T value) {
        try {
            property.set(value);
            config.save();
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
