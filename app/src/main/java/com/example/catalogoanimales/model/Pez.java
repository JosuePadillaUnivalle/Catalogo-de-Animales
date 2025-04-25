package com.example.catalogoanimales.model;

public class Pez extends Animal {
    private static final long serialVersionUID = 1L;
    private String tipoAgua;
    private String coloracion;
    private boolean esPredador;
    private double valor = 0.0;

    public Pez(String nombre, String especie, String habitat, String descripcion,
               String imagenUri, String categoria, String tipoAgua, String coloracion, boolean esPredador) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria);
        this.tipoAgua = tipoAgua;
        this.coloracion = coloracion;
        this.esPredador = esPredador;
        calcularValor();
    }

    @Override
    public void calcularValor() {
        // Valor base para peces
        double valorBase = 100.0;
        
        // Si es un tiburón, tiene un valor especial
        if (getNombre().toLowerCase().contains("tiburón")) {
            valorBase = 1000.0;
        }
        
        // Aumentar el valor si es depredador
        if (esPredador) {
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

    public String getTipoAgua() { return tipoAgua; }
    public void setTipoAgua(String tipoAgua) { this.tipoAgua = tipoAgua; }

    public String getColoracion() { return coloracion; }
    public void setColoracion(String coloracion) { this.coloracion = coloracion; }

    public boolean isEsPredador() { return esPredador; }
    public void setEsPredador(boolean esPredador) { this.esPredador = esPredador; }
} 