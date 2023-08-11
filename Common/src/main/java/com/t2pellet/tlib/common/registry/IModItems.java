package com.t2pellet.tlib.common.registry;

import net.minecraft.world.item.Item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Supplier;

public interface IModItems {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IItem {
        String value();
    }

    class TLibItem {
        private Supplier<Item> item = null;
        public final Item.Properties _properties;

        public Item getItem() {
            return item.get();
        }

        public TLibItem(Item.Properties properties) {
            this._properties = properties;
        }
    }


}
