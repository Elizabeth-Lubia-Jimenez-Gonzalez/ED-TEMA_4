package com.lubiasystem.menuestrnolineales.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.util.*;

public class GrafoController {
    @FXML private TextField txtOrigen;
    @FXML private TextField txtDestino;
    @FXML private TextArea txtAreaConsola;
    @FXML private Pane panelDibujo;

    // Colección del grafo traducida internamente
    private Map<String, List<String>> logisticsNetwork = new HashMap<>();

    @FXML
    public void initialize() {
        updateScreen();
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void agregarRuta() {
        String origin = txtOrigen.getText().trim().toUpperCase();
        String destination = txtDestino.getText().trim().toUpperCase();
        if (!origin.isEmpty() && !destination.isEmpty()) {
            logisticsNetwork.putIfAbsent(origin, new ArrayList<>());
            logisticsNetwork.putIfAbsent(destination, new ArrayList<>());
            if (!logisticsNetwork.get(origin).contains(destination)) logisticsNetwork.get(origin).add(destination);
            if (!logisticsNetwork.get(destination).contains(origin)) logisticsNetwork.get(destination).add(origin);
            updateScreen();
            txtOrigen.clear();
            txtDestino.clear();
        }
    }

    @FXML
    private void eliminarNodo() {
        String city = txtOrigen.getText().trim().toUpperCase();
        if (logisticsNetwork.containsKey(city)) {
            logisticsNetwork.remove(city);
            for (List<String> connections : logisticsNetwork.values()) {
                connections.remove(city);
            }
            updateScreen();
        }
    }

    @FXML
    private void consultarRuta() {
        String origin = txtOrigen.getText().trim().toUpperCase();
        String destination = txtDestino.getText().trim().toUpperCase();
        if (logisticsNetwork.containsKey(origin) && logisticsNetwork.get(origin).contains(destination)) {
            txtAreaConsola.setText(" Conexión directa existente.");
        } else {
            txtAreaConsola.setText(" No hay conexión directa.");
        }
    }

    @FXML
    private void mostrarMatriz() {
        StringBuilder sb = new StringBuilder("--- MATRIZ DE ADYACENCIA ---\n");
        for (String key : logisticsNetwork.keySet()) {
            sb.append(key).append(" -> ").append(logisticsNetwork.get(key).toString()).append("\n");
        }
        txtAreaConsola.setText(sb.toString());
    }

    @FXML
    private void limpiarGrafo() {
        logisticsNetwork.clear();
        updateScreen();
    }

    private void updateScreen() {
        panelDibujo.getChildren().clear();
        if (logisticsNetwork.isEmpty()) {
            txtAreaConsola.setText("[Grafo Vacío]");
            return;
        }

        StringBuilder sb = new StringBuilder("--- Lista de Adyacencia ---\n");
        Map<String, double[]> nodePositions = new HashMap<>();
        Random rand = new Random(42);

        for (String city : logisticsNetwork.keySet()) {
            sb.append(city).append(" con: ").append(logisticsNetwork.get(city).toString()).append("\n");
            double x = 50 + rand.nextDouble() * 350;
            double y = 50 + rand.nextDouble() * 350;
            nodePositions.put(city, new double[]{x, y});
        }
        txtAreaConsola.setText(sb.toString());

        Set<String> drawnEdges = new HashSet<>();
        for (String origin : logisticsNetwork.keySet()) {
            for (String destination : logisticsNetwork.get(origin)) {
                String edgeKey = origin + "-" + destination;
                String reverseEdgeKey = destination + "-" + origin;
                if (!drawnEdges.contains(edgeKey) && !drawnEdges.contains(reverseEdgeKey)) {
                    double[] p1 = nodePositions.get(origin);
                    double[] p2 = nodePositions.get(destination);
                    Line edgeLine = new Line(p1[0], p1[1], p2[0], p2[1]);
                    edgeLine.setStroke(Color.web("#cbd5e1"));
                    edgeLine.setStrokeWidth(2);
                    panelDibujo.getChildren().add(edgeLine);
                    drawnEdges.add(edgeKey);
                }
            }
        }

        for (String city : nodePositions.keySet()) {
            double[] coord = nodePositions.get(city);
            Circle nodeCircle = new Circle(coord[0], coord[1], 15, Color.web("#f59e0b"));
            Text labelText = new Text(coord[0] - 12, coord[1] + 4, city.substring(0, Math.min(city.length(), 3)));
            labelText.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
            panelDibujo.getChildren().addAll(nodeCircle, labelText);
        }
    }
}