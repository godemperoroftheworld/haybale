package com.t2pellet.tlib.client.compat;

import com.t2pellet.tlib.config.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class ForgeConfigMenu extends ConfigMenu {

    private interface IBuilderFactory<T, R extends TooltipListEntry<T>> {
        FieldBuilder<T, R> create(Component component, T value);
    }

    private interface IFieldBuilder<T, R extends TooltipListEntry<T>> {
        IFieldBuilder<T, R> setDefaultValue(T defaultValue);
        IFieldBuilder<T, R> setSaveConsumer(Consumer<T> consumer);
        IFieldBuilder<T, R> setTooltip(Component...tooltip);
        R build();
    }

    public void registerConfigMenu() {
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class,
                () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> {
                    try {
                        return configBuilder().build();
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }));
    }

    private ConfigBuilder configBuilder() throws IllegalAccessException {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslatableComponent("title." + modid + ".config"));
        for (Class<?> clazz : config.getClass().getDeclaredClasses()) {
            Config.Section section = clazz.getAnnotation(Config.Section.class);
            if (section != null) {
                addSection(builder, clazz, section.value());
            }
        }
        return builder;
    }

    private void addSection(ConfigBuilder configBuilder, Class<?> clazz, String name) throws IllegalAccessException {
        ConfigCategory category = configBuilder.getOrCreateCategory(new TextComponent(name));
        ConfigEntryBuilder builder = ConfigEntryBuilder.create();
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> type = field.getType();
            field.setAccessible(true);
            if (String.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startStrField, field));
            } else if (Integer.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startIntField, field));
            } else if (Boolean.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startBooleanToggle, field));
            } else if (Double.class.isAssignableFrom(type)) {
                category.addEntry(addField(builder::startDoubleField, field));
            } else if (List.class.isAssignableFrom(type) && isStringList(field)) {
                category.addEntry(addField(builder::startStrList, field));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T, R extends TooltipListEntry<T>> R addField(IBuilderFactory<T, R> builderFactory, Field field) throws IllegalAccessException {
        T value = (T) field.get(null);
        IFieldBuilder<T, R> fixedBuilder = (IFieldBuilder<T, R>) builderFactory.create(new TextComponent(field.getName()), value);
        fixedBuilder.setDefaultValue(value).setSaveConsumer(s -> setField(field, s));
        Config.Section.Comment comment = field.getAnnotation(Config.Section.Comment.class);
        if (comment != null) fixedBuilder.setTooltip(new TextComponent(comment.value()));
        return fixedBuilder.build();
    }

}
