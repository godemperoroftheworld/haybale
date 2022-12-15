package com.t2pellet.tlib.common.registry;

import com.t2pellet.tlib.Services;
import net.minecraft.world.item.Item;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.function.Supplier;

public interface IModItems {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface IItem {
        String value();
    }

    class TLibItem {
        public final Supplier<Item> ITEM = null;
        public final Item.Properties _properties;

        public TLibItem(Item.Properties properties) {
            this._properties = properties;
        }
    }


}
