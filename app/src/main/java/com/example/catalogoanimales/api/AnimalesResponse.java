package com.example.catalogoanimales.api;

import com.example.catalogoanimales.model.Animal;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AnimalesResponse {
    @SerializedName("animales")
    private List<Animal> animales;

    public List<Animal> getAnimales() {
        return animales;
    }

    public void setAnimales(List<Animal> animales) {
        this.animales = animales;
    }
} 