package com.example.catalogoanimales.model;

public class AveRapaz extends Ave {
    private static final long serialVersionUID = 1L;
    private double velocidadVuelo;
    private String tipoPresa;
    private double valor = 0.0;

    public AveRapaz(String nombre, String especie, String habitat, String descripcion,
                   String imagenUri, String categoria, double envergaduraAlas,
                   String colorPlumaje, String tipoPico, double velocidadVuelo,
                   String tipoPresa) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria,
              envergaduraAlas, colorPlumaje, tipoPico);
        this.velocidadVuelo = velocidadVuelo;
        this.tipoPresa = tipoPresa;
        calcularValor();
    }

    @Override
    public void calcularValor() {
        // Valor base para aves rapaces
        double valorBase = 200.0;
        
        // Aumentar el valor según la velocidad de vuelo
        valorBase += velocidadVuelo;
        
        setValor(valorBase);
    }

    public double getVelocidadVuelo() { return velocidadVuelo; }
    public void setVelocidadVuelo(double velocidadVuelo) { this.velocidadVuelo = velocidadVuelo; }

    public String getTipoPresa() { return tipoPresa; }
    public void setTipoPresa(String tipoPresa) { this.tipoPresa = tipoPresa; }

    @Override
    public AveRapaz clone() {
        AveRapaz cloned = (AveRapaz) super.clone();
        cloned.velocidadVuelo = this.velocidadVuelo;
        cloned.tipoPresa = this.tipoPresa;
        return cloned;
    }
} 