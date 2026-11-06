package com.lubiasystem.menuestrnolineales.controller;

import com.lubiasystem.menuestrnolineales.clases.Patient;
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

public class UrgenciasController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrioridad;
    @FXML private TextArea txtAreaConsola;
    @FXML private Pane panelDibujo;

    // Lista del heap simulado utilizando la clase de datos interna Patient
    private List<Patient> simulatedHeap = new ArrayList<>();

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
    private void registrarPaciente() {
        try {
            String patientName = txtNombre.getText();
            int priorityLevel = Integer.parseInt(txtPrioridad.getText());
            if (!patientName.isEmpty()) {
                simulatedHeap.add(new Patient(patientName, priorityLevel));
                simulatedHeap.sort(Comparator.comparingInt(Patient::getPriority)); // Mantiene el orden de prioridad del Heap
                updateScreen();
                txtNombre.clear();
                txtPrioridad.clear();
            }
        } catch (Exception e) {
            txtAreaConsola.setText("Error en prioridad.");
        }
    }

    @FXML
    private void atenderPaciente() {
        if (!simulatedHeap.isEmpty()) {
            Patient treatedPatient = simulatedHeap.remove(0);
            updateScreen();
            txtAreaConsola.appendText("\n\n [ATENDIDO]: " + treatedPatient.getName());
        }
    }

    @FXML
    private void verSiguiente() {
        if (!simulatedHeap.isEmpty()) {
            txtAreaConsola.setText("Frente: " + simulatedHeap.get(0).toString());
        }
    }

    @FXML
    private void contarFila() {
        txtAreaConsola.setText("En espera: " + simulatedHeap.size() + " pacientes.");
    }

    @FXML
    private void vaciarSala() {
        simulatedHeap.clear();
        updateScreen();
    }

    private void updateScreen() {
        panelDibujo.getChildren().clear();
        StringBuilder sb = new StringBuilder("--- Fila del Triage (Prioridad Heap) ---\n");
        for (Patient patient : simulatedHeap) {
            sb.append(patient.toString()).append("\n");
        }
        txtAreaConsola.setText(sb.toString());

        for (int i = 0; i < simulatedHeap.size(); i++) {
            double[] currentPosition = calculateHeapPosition(i);

            int leftChildIndex = 2 * i + 1;
            int rightChildIndex = 2 * i + 2;

            if (leftChildIndex < simulatedHeap.size()) {
                double[] leftChildPosition = calculateHeapPosition(leftChildIndex);
                panelDibujo.getChildren().add(new Line(currentPosition[0], currentPosition[1], leftChildPosition[0], leftChildPosition[1]));
            }
            if (rightChildIndex < simulatedHeap.size()) {
                double[] rightChildPosition = calculateHeapPosition(rightChildIndex);
                panelDibujo.getChildren().add(new Line(currentPosition[0], currentPosition[1], rightChildPosition[0], rightChildPosition[1]));
            }

            Circle nodeCircle = new Circle(currentPosition[0], currentPosition[1], 15, Color.web("#0284c7"));
            Text priorityText = new Text(currentPosition[0] - 5, currentPosition[1] + 4, String.valueOf(simulatedHeap.get(i).getPriority()));
            priorityText.setFill(Color.WHITE);
            panelDibujo.getChildren().addAll(nodeCircle, priorityText);
        }
    }

    private double[] calculateHeapPosition(int index) {
        int level = (int) (Math.log(index + 1) / Math.log(2));
        int positionInLevel = index - (int) Math.pow(2, level) + 1;
        double y = 40 + level * 60;
        double levelWidth = 450 / Math.pow(2, level);
        double x = levelWidth / 2 + positionInLevel * levelWidth;
        return new double[]{x, y};
    }
}
