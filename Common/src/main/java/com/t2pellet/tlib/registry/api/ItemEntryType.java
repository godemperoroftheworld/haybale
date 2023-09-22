package com.t2pellet.tlib.registry.api;

import net.minecraft.world.item.Item;

public class ItemEntryType extends EntryType<Item>  {

    private final String name;
    private final Item.Properties properties;

    public ItemEntryType(String name, Item.Properties properties) {
        super(Item.class);
        this.name = name;
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public Item.Properties getProperties() {
        return properties;
    }
}
