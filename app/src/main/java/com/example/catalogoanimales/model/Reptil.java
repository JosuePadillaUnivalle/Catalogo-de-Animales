package com.example.catalogoanimales.model;

public class Reptil extends Animal {
    private static final long serialVersionUID = 1L;
    private String tipoEscamas;
    private String tipoReproduccion;
    private boolean esVenenoso;
    private double valor = 0.0;

    public Reptil(String nombre, String especie, String habitat, String descripcion,
                 String imagenUri, String categoria, String tipoEscamas,
                 String tipoReproduccion, boolean esVenenoso) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria);
        this.tipoEscamas = tipoEscamas;
        this.tipoReproduccion = tipoReproduccion;
        this.esVenenoso = esVenenoso;
        calcularValor();
    }

    @Override
    public void calcularValor() {
        // Valor base para reptiles
        double valorBase = 100.0;
        
        // Si es un cocodrilo, tiene un valor especial
        if (getNombre().toLowerCase().contains("cocodrilo")) {
            valorBase = 1000.0;
        }
        
        // Aumentar el valor si es venenoso
        if (esVenenoso) {
            valorBase += 200.0;
        }
        
        setValor(valorBase);
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getTipoEscamas() { return tipoEscamas; }
    public void setTipoEscamas(String tipoEscamas) { this.tipoEscamas = tipoEscamas; }

    public String getTipoReproduccion() { return tipoReproduccion; }
    public void setTipoReproduccion(String tipoReproduccion) { this.tipoReproduccion = tipoReproduccion; }

    public boolean isEsVenenoso() { return esVenenoso; }
    public void setEsVenenoso(boolean esVenenoso) { this.esVenenoso = esVenenoso; }
} 