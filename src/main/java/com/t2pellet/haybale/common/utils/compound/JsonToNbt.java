package com.t2pellet.haybale.common.utils.compound;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import net.minecraft.nbt.*;

import java.util.Map;

/**
 * Json to NBT converter utility
 * Based off of <a href="https://github.com/KSashaDF/JSON-to-NBT-Converter">KSashaDF's repository</a>
 */
public class JsonToNbt {

    /**
     * Converts a JSON element to an NBT tag.
     *
     * @param jsonElement Element to convert.
     * @return The NBT tag equivalent. (imperfect in certain cases)
     */
    public static Tag toNbt(JsonElement jsonElement) {

        // JSON Primitive
        if (jsonElement instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;

            if (jsonPrimitive.isBoolean()) {
                boolean value = jsonPrimitive.getAsBoolean();

                if (value) {
                    return ByteTag.valueOf(true);
                } else {
                    return ByteTag.valueOf(false);
                }

            } else if (jsonPrimitive.isNumber()) {
                Number number = jsonPrimitive.getAsNumber();

                if (number instanceof Byte) {
                    return ByteTag.valueOf(number.byteValue());
                } else if (number instanceof Short) {
                    return ShortTag.valueOf(number.shortValue());
                } else if (number instanceof Integer) {
                    return IntTag.valueOf(number.intValue());
                } else if (number instanceof Long) {
                    return LongTag.valueOf(number.longValue());
                } else if (number instanceof Float) {
                    return FloatTag.valueOf(number.floatValue());
                } else if (number instanceof Double) {
                    return DoubleTag.valueOf(number.doubleValue());
                }

            } else if (jsonPrimitive.isString()) {
                return StringTag.valueOf(jsonPrimitive.getAsString());
            }

        } else if (jsonElement instanceof JsonArray jsonArray) {
            // JSON Array
            ListTag nbtList = new ListTag();

            for (JsonElement element : jsonArray) {
                nbtList.add(toNbt(element));
            }

            return nbtList;
        } else if (jsonElement instanceof JsonObject) {
            // JSON Object
            JsonObject jsonObject = (JsonObject) jsonElement;
            CompoundTag nbtCompound = new CompoundTag();

            for (Map.Entry<String, JsonElement> jsonEntry : jsonObject.entrySet()) {
                nbtCompound.put(jsonEntry.getKey(), toNbt(jsonEntry.getValue()));
            }

            return nbtCompound;

        // Null - Not fully supported
        } else if (jsonElement instanceof JsonNull) {
            return new CompoundTag();
        }

        // Something has gone wrong, throw an error.
        throw new AssertionError();
    }
}
