package com.t2pellet.haybale.common.utils.compound;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.*;

import java.util.Map;

public class NbtToJson {

    public static JsonElement toJson(Tag nbtElement) {
        return toJson(nbtElement, ConversionMode.RAW);
    }

    /**
     * Converts an NBT tag to a JSON element.
     *
     * @param nbtElement NBT tag to convert.
     * @param mode The conversion mode.
     * @return The JSON element equivalent. (imperfect in certain cases)
     */
    @SuppressWarnings("unchecked")
    public static JsonElement toJson(Tag nbtElement, ConversionMode mode) {

        // Numbers
        if (nbtElement instanceof NumericTag nbtNumber) {
            switch (mode) {
                case JSON: {
                    if (nbtNumber instanceof ByteTag nbtByte) {
                        byte value = nbtByte.asByte().orElseThrow();
                        switch (value) {
                            case 0: return new JsonPrimitive(false);
                            case 1: return new JsonPrimitive(true);
                            default: // Continue
                        }
                    }
                    // Else, continue
                }
                case RAW: {
                    return new JsonPrimitive(nbtNumber.asNumber().orElseThrow());
                }
            }
        } else if (nbtElement instanceof StringTag nbtString) {
            // String
            return new JsonPrimitive(nbtString.asString().orElseThrow());
        } else if (nbtElement instanceof ListTag nbtList) {
            // Lists
            JsonArray jsonArray = new JsonArray();
            for (Tag nbtBase : nbtList) {
                jsonArray.add(toJson(nbtBase, mode));
            }

            return jsonArray;

        } else if (nbtElement instanceof CompoundTag nbtCompound) {
            // Compound tag
            JsonObject jsonObject = new JsonObject();

            for (Map.Entry<String, Tag> nbtEntry : nbtCompound.entrySet()) {
                jsonObject.add(nbtEntry.getKey(), toJson(nbtEntry.getValue(), mode));
            }

            return jsonObject;

            // Nbt termination tag. Should not be encountered.
        } else if (nbtElement instanceof EndTag) {
            throw new AssertionError();
        }

        // Impossible unless a new NBT class is made.
        throw new UnsupportedOperationException();
    }

    /**
     * The NBT to JSON conversion mode.
     */
    public enum ConversionMode {

        /**
         * The NBT will be converted to JSON 'as is'.
         */
        RAW,
        /**
         * The NBT will be converted to JSOn with the assumption that it
         * was previously JSON, and therefore certain assumptions and
         * conversions will be made.
         *
         * Conversions:
         *
         * 0b -> false
         * 1b -> true
         */
        JSON
    }
}
