package com.lubiasystem.logigrap.clases;

public class Nodo implements Comparable<Nodo> {
    private String nombre;
    private int distanciaMinima = Integer.MAX_VALUE;
    private Nodo nodoAnterior = null;

    public Nodo(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }

    public int getDistanciaMinima() { return distanciaMinima; }
    public void setDistanciaMinima(int distanciaMinima) { this.distanciaMinima = distanciaMinima; }

    public Nodo getNodoAnterior() { return nodoAnterior; }
    public void setNodoAnterior(Nodo nodoAnterior) { this.nodoAnterior = nodoAnterior; }

    @Override
    public String toString() { return nombre; }

    @Override
    public int compareTo(Nodo otro) {
        return Integer.compare(this.distanciaMinima, otro.distanciaMinima);
    }
}

