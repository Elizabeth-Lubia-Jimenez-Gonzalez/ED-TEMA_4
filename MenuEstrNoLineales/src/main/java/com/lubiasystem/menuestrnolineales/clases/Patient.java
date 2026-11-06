package com.lubiasystem.menuestrnolineales.clases;

public class Patient implements Comparable<Patient> {
    private String name;
    private int priority;

    public Patient(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    public String getName() { return name; }
    public int getPriority() { return priority; }

    @Override
    public int compareTo(Patient other) {
        // Un número menor de prioridad (ej: Triage 1) se atiende ANTES.
        return Integer.compare(this.priority, other.priority);
    }

    @Override
    public String toString() {
        return "[Nivel " + priority + "] " + name;
    }
}