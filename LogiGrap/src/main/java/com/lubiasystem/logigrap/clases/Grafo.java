package com.lubiasystem.logigrap.clases;

import java.util.*;

public class Grafo {
    private List<Nodo> ciudades;
    private List<Arista> carreteras;

    public Grafo() {
        this.ciudades = new ArrayList<>();
        this.carreteras = new ArrayList<>();
    }

    public void agregarCiudad(String nombre) {
        if (buscarCiudad(nombre) == null) {
            ciudades.add(new Nodo(nombre));
        }
    }

    public void agregarCarretera(String origen, String destino, int distancia) {
        Nodo cOrig = buscarCiudad(origen);
        Nodo cDest = buscarCiudad(destino);
        if (cOrig != null && cDest != null) {
            carreteras.add(new Arista(cOrig, cDest, distancia));
            carreteras.add(new Arista(cDest, cOrig, distancia));
        }
    }

    public Nodo buscarCiudad(String nombre) {
        return ciudades.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    public List<Nodo> getCiudades() { return ciudades; }

    public String calcularRutaMinimaDijkstra(String nombreOrigen, String nombreDestino) {
        Nodo origen = buscarCiudad(nombreOrigen);
        Nodo destino = buscarCiudad(nombreDestino);

        if (origen == null || destino == null) return "Error: Nodos no válidos.";
        if (origen.equals(destino)) return "Ya te encuentras en la ciudad de destino.";

        for (Nodo n : ciudades) {
            n.setDistanciaMinima(Integer.MAX_VALUE);
            n.setNodoAnterior(null);
        }
        origen.setDistanciaMinima(0);

        PriorityQueue<Nodo> filaPrioridad = new PriorityQueue<>();
        filaPrioridad.add(origen);
        Set<Nodo> visitados = new HashSet<>();

        while (!filaPrioridad.isEmpty()) {
            Nodo actual = filaPrioridad.poll();
            if (!visitados.add(actual)) continue;

            for (Arista arista : carreteras) {
                if (arista.getOrigen().equals(actual)) {
                    Nodo vecino = arista.getDestino();
                    if (!visitados.contains(vecino)) {
                        int nuevaDistancia = actual.getDistanciaMinima() + arista.getPeso();
                        if (nuevaDistancia < vecino.getDistanciaMinima()) {
                            filaPrioridad.remove(vecino);
                            vecino.setDistanciaMinima(nuevaDistancia);
                            vecino.setNodoAnterior(actual);
                            filaPrioridad.add(vecino);
                        }
                    }
                }
            }
        }

        if (destino.getDistanciaMinima() == Integer.MAX_VALUE) {
            return "No existe ninguna ruta vial disponible entre " + nombreOrigen + " y " + nombreDestino + ".";
        }

        List<Nodo> ruta = new ArrayList<>();
        for (Nodo n = destino; n != null; n = n.getNodoAnterior()) {
            ruta.add(n);
        }
        Collections.reverse(ruta);

        StringBuilder sb = new StringBuilder();
        sb.append("🎯 RUTA ÓPTIMA (Algoritmo de Dijkstra):\n");
        sb.append("──────────────────────────────────────────\n");
        for (int i = 0; i < ruta.size(); i++) {
            sb.append(ruta.get(i).getNombre());
            if (i < ruta.size() - 1) sb.append(" ➔ ");
        }
        sb.append("\n\n📊 Distancia Mínima Total: ").append(destino.getDistanciaMinima()).append(" Km\n");
        return sb.toString();
    }

    public String getMatrizAdyacenciaTexto() {
        if (ciudades.isEmpty()) return "Matriz vacía. Registra ciudades.";

        StringBuilder sb = new StringBuilder();
        sb.append("📊 MATRIZ DE ADYACENCIA (Estructura interna)\n");
        sb.append("──────────────────────────────────────────\n\t");

        for (Nodo c : ciudades) {
            sb.append(c.getNombre()).append("\t");
        }
        sb.append("\n");

        for (Nodo fila : ciudades) {
            sb.append(fila.getNombre()).append("\t");
            for (Nodo col : ciudades) {
                if (fila.equals(col)) {
                    sb.append("0\t");
                } else {
                    int pesoEncontrado = 0;
                    for (Arista a : carreteras) {
                        if (a.getOrigen().equals(fila) && a.getDestino().equals(col)) {
                            pesoEncontrado = a.getPeso();
                            break;
                        }
                    }
                    if (pesoEncontrado > 0) {
                        sb.append(pesoEncontrado).append("\t");
                    } else {
                        sb.append("∞\t");
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

