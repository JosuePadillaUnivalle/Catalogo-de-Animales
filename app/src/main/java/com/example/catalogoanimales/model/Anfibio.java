package com.example.catalogoanimales.model;

public class Anfibio extends Animal {
    private static final long serialVersionUID = 1L;
    private String tipoPiel;
    private String etapaVida;
    private boolean esVenenoso;
    private double valor = 0.0;

    public Anfibio(String nombre, String especie, String habitat, String descripcion,
                  String imagenUri, String categoria, String tipoPiel, boolean esVenenoso) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria);
        this.tipoPiel = tipoPiel;
        this.esVenenoso = esVenenoso;
        this.etapaVida = "Adulto"; // Valor por defecto
        calcularValor();
    }

    @Override
    public void calcularValor() {
        // Valor base para anfibios
        double valorBase = 100.0;
        
        // Si es venenoso, tiene un valor adicional
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

    public String getTipoPiel() { return tipoPiel; }
    public void setTipoPiel(String tipoPiel) { this.tipoPiel = tipoPiel; }

    public String getEtapaVida() { return etapaVida; }
    public void setEtapaVida(String etapaVida) { this.etapaVida = etapaVida; }

    public boolean isEsVenenoso() { return esVenenoso; }
    public void setEsVenenoso(boolean esVenenoso) { this.esVenenoso = esVenenoso; }
} 