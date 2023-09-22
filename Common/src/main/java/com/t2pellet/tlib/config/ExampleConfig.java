package com.t2pellet.tlib.config;

import com.t2pellet.tlib.TenzinLib;
import com.t2pellet.tlib.config.api.Config;
import com.t2pellet.tlib.config.api.property.*;

import java.io.IOException;
import java.util.List;

@Config.ModConfig(comment = "This is a config")
public class ExampleConfig extends Config {

    public ExampleConfig() throws IOException, IllegalAccessException {
        super(TenzinLib.MODID);
    }

    @Section(name = "Section One", description = "Is a very cool section")
    public static class SectionOne {
        @Entry(comment = "A test int")
        public static final IntProperty testInt = new IntProperty(5);
        @Entry(comment = "A test float")
        public static final FloatProperty testFloat = new FloatProperty(4.2F);
        @Entry(comment = "A test boolean")
        public static final BoolProperty testBool = new BoolProperty(false);
    }

    @Section(name = "Section Two", description = "Not as cool tbh")
    public static class SectionTwo {
        @Entry(comment = "A string")
        public static final StringProperty testString = new StringProperty("asdf");
        @Entry(comment = "A list. i hope this works")
        public static final ListProperty<Float> testFloatList = ListProperty.of(PropertyType.FLOAT, List.of(4.2F, 3.6F));
        @Entry(comment = "A string list. You can add things in double quotes but it will revert to a state like this one. Note the escape character for :")
        public static final ListProperty<String> testStringList = ListProperty.of(PropertyType.STRING, List.of("asdf", "efg", "weirD:chara!cters"));
    }
}
