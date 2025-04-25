package com.example.catalogoanimales.model;

public class Mamifero extends Animal {
    private static final long serialVersionUID = 1L;
    private double temperaturaCorporal;
    private int tiempoGestacion;
    private String tipoAlimentacion;
    private String tipoPelaje;
    private boolean esNocturno;
    private double valor = 0.0;

    public Mamifero(String nombre, String especie, String habitat, String descripcion, 
                    String imagenUri, String categoria, double temperaturaCorporal, 
                    int tiempoGestacion, String tipoAlimentacion) {
        super(nombre, especie, habitat, descripcion, imagenUri, categoria);
        this.temperaturaCorporal = temperaturaCorporal;
        this.tiempoGestacion = tiempoGestacion;
        this.tipoAlimentacion = tipoAlimentacion;
        this.tipoPelaje = "";
        this.esNocturno = false;
        calcularValor();
    }

    @Override
    public void calcularValor() {
        if (getNombre().toLowerCase().contains("león")) {
            valor = 1000.0;
        }
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getTemperaturaCorporal() {
        return temperaturaCorporal;
    }

    public void setTemperaturaCorporal(double temperaturaCorporal) {
        this.temperaturaCorporal = temperaturaCorporal;
    }

    public int getTiempoGestacion() {
        return tiempoGestacion;
    }

    public void setTiempoGestacion(int tiempoGestacion) {
        this.tiempoGestacion = tiempoGestacion;
    }

    public String getTipoAlimentacion() {
        return tipoAlimentacion;
    }

    public void setTipoAlimentacion(String tipoAlimentacion) {
        this.tipoAlimentacion = tipoAlimentacion;
    }

    public String getTipoPelaje() {
        return tipoPelaje;
    }

    public void setTipoPelaje(String tipoPelaje) {
        this.tipoPelaje = tipoPelaje;
    }

    public boolean isEsNocturno() {
        return esNocturno;
    }

    public void setEsNocturno(boolean esNocturno) {
        this.esNocturno = esNocturno;
    }
} 