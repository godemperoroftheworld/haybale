package com.t2pellet.haybale.client.registry.api;

import com.t2pellet.haybale.registry.api.EntryType;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class EntityModelEntryType extends EntryType<ModelLayerLocation> {

    private final String name;
    private final LayerDefinition layerDefinition;

    public EntityModelEntryType(String name, LayerDefinition layerDefinition) {
        super(ModelLayerLocation.class);
        this.name = name;
        this.layerDefinition = layerDefinition;
    }

    public String getName() {
        return name;
    }

    public LayerDefinition getLayerDefinition() {
        return layerDefinition;
    }
}
