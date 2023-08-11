package com.t2pellet.tlib.client.compat;

import com.t2pellet.tlib.config.Config;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.TooltipListEntry;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

public class FabricConfigMenu extends ConfigMenu implements ModMenuApi {

    private interface IBuilderFactory<T, R extends TooltipListEntry<T>, S extends FieldBuilder<T, R, S>> {
        FieldBuilder<T, R, S> create(Component component, T value);
    }

    private interface IFieldBuilder<T, R extends TooltipListEntry<T>> {
        IFieldBuilder<T, R> setDefaultValue(T defaultValue);
        IFieldBuilder<T, R> setSaveConsumer(Consumer<T> consumer);
        IFieldBuilder<T, R> setTooltip(Component...tooltip);
        R build();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            try {
                return configBuilder().build();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        };
    }

    private ConfigBuilder configBuilder() throws IllegalAccessException {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(Component.translatable("title." + modid + ".config"));
        for (Class<?> clazz : config.getClass().getDeclaredClasses()) {
            Config.Section section = clazz.getAnnotation(Config.Section.class);
            if (section != null) {
                addSection(builder, clazz, section.value());
            }
        }
        return builder;
    }

    private void addSection(ConfigBuilder configBuilder, Class<?> clazz, String name) throws IllegalAccessException {
        ConfigCategory category = configBuilder.getOrCreateCategory(Component.literal(name));
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
    private <T, R extends TooltipListEntry<T>, S extends FieldBuilder<T, R, S>> R addField(IBuilderFactory<T, R, S> builderFactory, Field field) throws IllegalAccessException {
        T value = (T) field.get(null);
        IFieldBuilder<T, R> fixedBuilder = (IFieldBuilder<T, R>) builderFactory.create(Component.literal(field.getName()), value);
        fixedBuilder.setDefaultValue(value).setSaveConsumer(s -> setField(field, s));
        Config.Section.Comment comment = field.getAnnotation(Config.Section.Comment.class);
        if (comment != null) fixedBuilder.setTooltip(Component.literal(comment.value()));
        return fixedBuilder.build();
    }
}
