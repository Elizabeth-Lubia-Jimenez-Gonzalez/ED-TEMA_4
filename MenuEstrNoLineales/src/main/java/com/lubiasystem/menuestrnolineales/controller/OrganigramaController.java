package com.lubiasystem.menuestrnolineales.controller;

import com.lubiasystem.menuestrnolineales.clases.Employee;
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

public class OrganigramaController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtPuesto;
    @FXML private TextArea txtAreaConsola;
    @FXML private Pane panelDibujo;

    private NodeElement corporateRoot;
    // Variable para almacenar temporalmente el puesto que queremos resaltar de color
    private String highlightedPosition = "";

    private static class NodeElement {
        Employee employee;
        List<NodeElement> subordinates = new ArrayList<>();
        NodeElement(Employee emp) { this.employee = emp; }
    }

    @FXML
    public void initialize() {
        corporateRoot = new NodeElement(new Employee("Carlos Slim", "CEO"));
        updateScreen();
    }

    @FXML
    private void regresarMenu(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void agregarEmpleado() {
        String name = txtNombre.getText();
        String position = txtPuesto.getText();
        if (!name.isEmpty() && !position.isEmpty()) {
            corporateRoot.subordinates.add(new NodeElement(new Employee(name, position)));
            highlightedPosition = ""; // Limpia búsquedas anteriores al insertar
            updateScreen();
            txtNombre.clear(); txtPuesto.clear();
        }
    }

    @FXML
    private void eliminarEmpleado() {
        if (!corporateRoot.subordinates.isEmpty()) {
            corporateRoot.subordinates.remove(corporateRoot.subordinates.size() - 1);
            highlightedPosition = "";
            updateScreen();
        } else {
            txtAreaConsola.setText("No puedes eliminar al CEO.");
        }
    }

    // BOTÓN 3: Buscar (Corregido para recorrer todo el árbol y mandar color)
    @FXML
    private void buscarPuesto() {
        String query = txtPuesto.getText().trim().toUpperCase();
        if (query.isEmpty()) {
            txtAreaConsola.setText("Error: Ingrese un puesto para buscar.");
            return;
        }

        Employee foundEmployee = null;

        // 1. Verificar si es el CEO
        if (corporateRoot.employee.getPosition().toUpperCase().contains(query)) {
            foundEmployee = corporateRoot.employee;
        } else {
            // 2. Recorrer los subordinados buscando el puesto
            for (NodeElement sub : corporateRoot.subordinates) {
                if (sub.employee.getPosition().toUpperCase().contains(query)) {
                    foundEmployee = sub.employee;
                    break;
                }
            }
        }

        // 3. Mostrar resultado en consola y activar el resaltado gráfico
        if (foundEmployee != null) {
            txtAreaConsola.setText("🔍 ENCONTRADO EN EL ORGANIGRAMA:\n" + foundEmployee.toString());
            highlightedPosition = foundEmployee.getPosition(); // Guarda el puesto para pintarlo
            renderVisualGraph(); // Redibuja aplicando el color
        } else {
            txtAreaConsola.setText("❌ No se encontró coincidencia para el puesto: " + txtPuesto.getText());
            highlightedPosition = "";
            renderVisualGraph();
        }
    }

    @FXML private void expandirArbol() { txtAreaConsola.setText(" Vista Gráfica Desplegada por Completo a la derecha."); }
    @FXML private void colapsarArbol() { panelDibujo.getChildren().clear(); txtAreaConsola.setText(" Gráfica Colapsada / Oculta."); }

    private void updateScreen() {
        StringBuilder sb = new StringBuilder("--- ESTRUCTURA CORPORATIVA LINEAL ---\n\n");
        sb.append("• ").append(corporateRoot.employee.toString()).append("\n");
        for(NodeElement sub : corporateRoot.subordinates) {
            sb.append("   └── ").append(sub.employee.toString()).append("\n");
        }
        txtAreaConsola.setText(sb.toString());

        renderVisualGraph();
    }

    // Método auxiliar para limpiar y redibuja la gráfica
    private void renderVisualGraph() {
        panelDibujo.getChildren().clear();
        drawVisualTree(corporateRoot, 250, 40, 70);
    }

    private void drawVisualTree(NodeElement node, double x, double y, double xOffset) {
        Circle circle = new Circle(x, y, 16);

        // LÓGICA DE COLOR: Si el puesto coincide con la búsqueda actual, se pinta de Amarillo/Naranja
        if (!highlightedPosition.isEmpty() && node.employee.getPosition().equalsIgnoreCase(highlightedPosition)) {
            circle.setFill(Color.web("#f59e0b")); // Color de resaltado (Búsqueda exitosa)
        } else {
            circle.setFill(Color.web("#6366f1")); // Color morado base original
        }

        circle.setStroke(Color.DARKBLUE);
        Text text = new Text(x - 12, y + 4, node.employee.getName().substring(0, Math.min(node.employee.getName().length(), 4)));
        text.setFill(Color.WHITE);
        panelDibujo.getChildren().addAll(circle, text);

        double startX = x - (node.subordinates.size() - 1) * xOffset / 2;
        for (int i = 0; i < node.subordinates.size(); i++) {
            double childX = startX + i * xOffset;
            double childY = y + 70;
            Line line = new Line(x, y + 16, childX, childY - 16);
            panelDibujo.getChildren().add(line);
            drawVisualTree(node.subordinates.get(i), childX, childY, xOffset * 0.6);
        }
    }
}
