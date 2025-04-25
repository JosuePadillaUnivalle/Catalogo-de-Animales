package com.example.catalogoanimales.model;

public class Ave extends Animal {
    private static final long serialVersionUID = 1L;
    private double envergaduraAlas;
    private String colorPlumaje;
    private String tipoPico;
    private String tipoVuelo;
    private boolean puedeVolar;
    private double valor = 0.0;

    public Ave(String nombre, String especie, String habitat, String descripcion,
              String imagenUri, String categoria, double envergaduraAlas,
              String colorPlumaje, String tipoPico) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria);
        this.envergaduraAlas = envergaduraAlas;
        this.colorPlumaje = colorPlumaje;
        this.tipoPico = tipoPico;
        this.tipoVuelo = "Normal";
        this.puedeVolar = true;
        calcularValor();
    }

    @Override
    public void calcularValor() {
        // Valor base para aves
        double valorBase = 100.0;
        
        // Si es un águila, tiene un valor especial
        if (getNombre().toLowerCase().contains("águila")) {
            valorBase = 1000.0;
        }
        
        // Aumentar el valor según la envergadura
        valorBase += envergaduraAlas * 10;
        
        setValor(valorBase);
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getEnvergaduraAlas() { return envergaduraAlas; }
    public void setEnvergaduraAlas(double envergaduraAlas) { this.envergaduraAlas = envergaduraAlas; }

    public String getColorPlumaje() { return colorPlumaje; }
    public void setColorPlumaje(String colorPlumaje) { this.colorPlumaje = colorPlumaje; }

    public String getTipoPico() { return tipoPico; }
    public void setTipoPico(String tipoPico) { this.tipoPico = tipoPico; }

    public String getTipoVuelo() { return tipoVuelo; }
    public void setTipoVuelo(String tipoVuelo) { this.tipoVuelo = tipoVuelo; }

    public boolean isPuedeVolar() { return puedeVolar; }
    public void setPuedeVolar(boolean puedeVolar) { this.puedeVolar = puedeVolar; }
} 