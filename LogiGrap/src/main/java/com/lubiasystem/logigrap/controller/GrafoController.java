package com.lubiasystem.logigrap.controller;

import com.lubiasystem.logigrap.clases.Grafo;
import com.lubiasystem.logigrap.clases.Nodo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GrafoController {

    @FXML private TextField txtNuevaCiudad;
    @FXML private ComboBox<Nodo> cmbOrigenCarretera;
    @FXML private ComboBox<Nodo> cmbDestinoCarretera;
    @FXML private TextField txtDistancia;

    @FXML private ComboBox<Nodo> cmbOrigenViaje;
    @FXML private ComboBox<Nodo> cmbDestinoViaje;
    @FXML private TextArea txtResultados;

    private final Grafo sistemaRutas = new Grafo();
    private final ObservableList<Nodo> listaCiudadesVisual = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        cmbOrigenCarretera.setItems(listaCiudadesVisual);
        cmbDestinoCarretera.setItems(listaCiudadesVisual);
        cmbOrigenViaje.setItems(listaCiudadesVisual);
        cmbDestinoViaje.setItems(listaCiudadesVisual);

        //MEJORA DE UX: Guía interactiva detallada para el usuario final en la consola principal
        txtResultados.setText("=== BIENVENIDO A LOGIGRAPH v1.0 ===\n" +
                "Guía de uso rápido para el operador:\n\n" +
                "1️⃣ REGISTRAR CIUDADES: Vaya a la casilla '1. Registrar Ciudad', escriba un nombre (ej. 'MADRID' o 'A') y presione el botón negro.\n\n" +
                "2️⃣ CREAR CARRETERAS: Una vez creadas las ciudades, selecciónelas en los menús del paso 2, escriba la distancia en kilómetros (solo números) y presione 'Conectar'.\n\n" +
                "3️⃣ CALCULAR RUTA: En el panel derecho, elija su punto de partida y de llegada, luego presione el botón azul para que el algoritmo de Dijkstra calcule el trayecto más económico.");
    }

    @FXML
    void guardarCiudad() {
        String nombre = txtNuevaCiudad.getText().trim().toUpperCase();
        if (!nombre.isEmpty()) {
            if (sistemaRutas.buscarCiudad(nombre) == null) {
                sistemaRutas.agregarCiudad(nombre);
                actualizarCombosVisuales();
                txtResultados.setText("[CIUDAD GUARDADA]: " + nombre + "\n\nSiguiente acción recomendada:\nRegistre otra ciudad o pase al PASO 2 para conectarlas.");
                txtNuevaCiudad.clear();
            } else {
                txtResultados.setText("ERROR: La ciudad '" + nombre + "' ya existe en el sistema. Ingrese un nombre diferente.");
            }
        } else {
            txtResultados.setText("⚠️ AVISO: El campo de nombre de ciudad no puede estar vacío. Por favor, escriba una palabra.");
        }
    }

    @FXML
    void guardarCarretera() {
        Nodo origen = cmbOrigenCarretera.getValue();
        Nodo destino = cmbDestinoCarretera.getValue();
        String distanciaStr = txtDistancia.getText().trim();

        if (origen == null || destino == null || distanciaStr.isEmpty()) {
            txtResultados.setText("⚠️ DATOS INCOMPLETOS: Para crear una carretera debe:\n1. Elegir un Origen.\n2. Elegir un Destino.\n3. Escribir los Kilómetros.");
            return;
        }

        if (origen.equals(destino)) {
            txtResultados.setText("OPERACIÓN INVÁLIDA: No se puede conectar una ciudad consigo misma. Elija un destino diferente.");
            return;
        }

        try {
            int distancia = Integer.parseInt(distanciaStr);
            if (distancia <= 0) {
                txtResultados.setText("ERROR MATEMÁTICO: Los kilómetros deben ser un número positivo mayor a 0.");
                return;
            }

            sistemaRutas.agregarCarretera(origen.getNombre(), destino.getNombre(), distancia);
            txtResultados.setText("[CONEXIÓN EXITOSA]: Se ha creado una carretera entre " + origen + " y " + destino + " de " + distancia + " Km.\n\nYa puede calcular rutas que pasen por este tramo.");
            txtDistancia.clear();

        } catch (NumberFormatException e) {
            txtResultados.setText("TIPO DE DATO ERRÓNEO: En el campo de distancia debe escribir exclusivamente números enteros (Ej. 80). No se admiten letras.");
        }
    }

    @FXML
    void calcularRuta() {
        Nodo origen = cmbOrigenViaje.getValue();
        Nodo destino = cmbDestinoViaje.getValue();

        if (origen != null && destino != null) {
            String informeRuta = sistemaRutas.calcularRutaMinimaDijkstra(origen.getNombre(), destino.getNombre());
            txtResultados.setText(informeRuta);
        } else {
            txtResultados.setText("SELECCIÓN REQUERIDA: Elija una Bodega/Salida y un Cliente/Destino en los menús desplegables para poder ejecutar el cálculo.");
        }
    }

    @FXML
    void verMatrizAdyacencia() {
        String matriz = sistemaRutas.getMatrizAdyacenciaTexto();
        txtResultados.setText(matriz);
    }

    private void actualizarCombosVisuales() {
        listaCiudadesVisual.clear();
        listaCiudadesVisual.addAll(sistemaRutas.getCiudades());
    }
}