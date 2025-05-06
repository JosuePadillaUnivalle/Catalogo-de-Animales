package com.example.catalogoanimales.model;

import java.io.Serializable;

public abstract class Animal implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String nombre;
    private String especie;
    private String nombreCientifico;
    private String habitat;
    private String descripcion;
    private String imagenUri;
    private String categoria;
    private String estadoConservacion;
    private double pesoPromedio;
    private boolean isPredeterminado;
    protected double valor = 0.0;
    private int esperanzaVida;
    private double velocidadMaxima;
    private double alturaPromedio;

    public Animal(String nombre, String especie, String habitat, String descripcion, String imagenUri, String categoria) {
        this.id = java.util.UUID.randomUUID().toString(); // Generamos un ID único al crear el animal
        this.nombre = nombre;
        this.especie = especie;
        this.habitat = habitat;
        this.descripcion = descripcion;
        this.imagenUri = imagenUri;
        this.categoria = categoria;
        this.isPredeterminado = false;
        calcularValor();
        this.nombreCientifico = "";
        this.estadoConservacion = "No especificado";
        this.pesoPromedio = 0.0;
    }

    // Método para calcular el valor del animal
    public void calcularValor() {
        // Por defecto, el valor es 0
        // Las clases hijas pueden sobrescribir este método para implementar su propia lógica
        this.valor = 0.0;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getHabitat() { return habitat; }
    public void setHabitat(String habitat) { this.habitat = habitat; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUri() { return imagenUri; }
    public void setImagenUri(String imagenUri) { this.imagenUri = imagenUri; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isPredeterminado() { return isPredeterminado; }
    public void setPredeterminado(boolean predeterminado) { isPredeterminado = predeterminado; }

    public String getNombreCientifico() { return nombreCientifico; }
    public void setNombreCientifico(String nombreCientifico) { this.nombreCientifico = nombreCientifico; }

    public String getEstadoConservacion() { return estadoConservacion; }
    public void setEstadoConservacion(String estadoConservacion) { this.estadoConservacion = estadoConservacion; }

    public double getPesoPromedio() { return pesoPromedio; }
    public void setPesoPromedio(double pesoPromedio) { this.pesoPromedio = pesoPromedio; }

    public int getEsperanzaVida() { return esperanzaVida; }
    public void setEsperanzaVida(int esperanzaVida) { this.esperanzaVida = esperanzaVida; }

    public double getVelocidadMaxima() { return velocidadMaxima; }
    public void setVelocidadMaxima(double velocidadMaxima) { this.velocidadMaxima = velocidadMaxima; }

    public double getAlturaPromedio() { return alturaPromedio; }
    public void setAlturaPromedio(double alturaPromedio) { this.alturaPromedio = alturaPromedio; }

    @Override
    public Animal clone() {
        try {
            Animal cloned = (Animal) super.clone();
            cloned.id = this.id;
            cloned.nombre = this.nombre;
            cloned.especie = this.especie;
            cloned.nombreCientifico = this.nombreCientifico;
            cloned.habitat = this.habitat;
            cloned.descripcion = this.descripcion;
            cloned.imagenUri = this.imagenUri;
            cloned.categoria = this.categoria;
            cloned.estadoConservacion = this.estadoConservacion;
            cloned.pesoPromedio = this.pesoPromedio;
            cloned.isPredeterminado = this.isPredeterminado;
            cloned.valor = this.valor;
            cloned.esperanzaVida = this.esperanzaVida;
            cloned.velocidadMaxima = this.velocidadMaxima;
            cloned.alturaPromedio = this.alturaPromedio;
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar el animal", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id != null && id.equals(animal.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 