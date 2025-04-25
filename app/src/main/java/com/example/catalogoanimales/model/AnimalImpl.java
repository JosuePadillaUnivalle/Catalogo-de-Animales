package com.example.catalogoanimales.model;

public class AnimalImpl extends Animal {
    public AnimalImpl() {
        super("", "", "", "", "", "");
    }

    public AnimalImpl(String nombre, String especie, String habitat, String descripcion, String imagenUri, String categoria) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria);
    }

    @Override
    public void calcularValor() {
        // Implementación simple del cálculo de valor
        setValor(1.0);
    }
} 