package com.example.catalogoanimales.interfaces;

import com.example.catalogoanimales.model.Animal;

public interface OnAnimalCategoryChangedListener {
    void onAnimalCategoryChanged(Animal animal, String oldCategory, String newCategory);
} 