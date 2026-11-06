package com.lubiasystem.menuestrnolineales.controller;

import com.lubiasystem.menuestrnolineales.clases.Product;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BstController {
    @FXML private TextField txtId;
    @FXML private TextField txtNombre;
    @FXML private TextArea txtAreaConsola;
    @FXML private Pane panelDibujo;

    // Lista nativa ordenada para emular un BST balanceado para la renderización visual
    private List<Product> productList = new ArrayList<>();

    @FXML
    public void initialize() {
        updateScreen();
    }

    // BOTÓN 1: Insertar
    @FXML
    private void agregarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String name = txtNombre.getText();
            if (!name.isEmpty()) {
                Product newProduct = new Product(id, name);
                if (!productList.contains(newProduct)) {
                    productList.add(newProduct);
                    Collections.sort(productList); // Mantiene el orden de árbol BST
                    updateScreen();
                } else {
                    txtAreaConsola.setText("⚠️ El ID " + id + " ya está registrado en el inventario.");
                }
                txtId.clear(); txtNombre.clear();
            }
        } catch (NumberFormatException e) {
            txtAreaConsola.setText("Error: Ingrese un ID numérico.");
        }
    }

    // BOTÓN 2: Eliminar
    @FXML
    private void eliminarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Product keyProduct = new Product(id, "");
            if (productList.remove(keyProduct)) {
                txtAreaConsola.setText(" Producto con ID " + id + " eliminado correctamente.");
                updateScreen();
            } else {
                txtAreaConsola.setText(" No se encontró ningún producto con el ID: " + id);
            }
            txtId.clear();
        } catch (NumberFormatException e) {
            txtAreaConsola.setText("Error: Ingrese un ID numérico válido para eliminar.");
        }
    }

    // BOTÓN 3: Buscar
    @FXML
    private void buscarProducto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            for (Product p : productList) {
                if (p.getId() == id) {
                    txtAreaConsola.setText(" ENCONTRADO EN INVENTARIO:\n" + p.toString());
                    drawBstGraph(id);
                    return;
                }
            }
            txtAreaConsola.setText(" El producto con ID " + id + " no existe.");
        } catch (NumberFormatException e) {
            txtAreaConsola.setText("Error: Ingrese un ID numérico para buscar.");
        }
    }

    // BOTÓN 4: Ver Gráfica en Texto Esquemático
    @FXML
    private void mostrarGrafica() {
        StringBuilder sb = new StringBuilder("--- REPRESENTACIÓN ESQUEMÁTICA EN TEXTO ---\n\n");
        if(productList.isEmpty()) {
            sb.append("[Árbol Vacío]");
        } else {
            sb.append(" (Raíz del Inventario)\n");
            for (Product p : productList) {
                sb.append("   ├── [ID: ").append(p.getId()).append("] -> ").append(p.getName()).append("\n");
            }
        }
        txtAreaConsola.setText(sb.toString());
        drawBstGraph(-1);
    }

    // BOTÓN 5: Vaciar
    @FXML
    private void vaciarArbol() {
        productList.clear();
        updateScreen();
        txtAreaConsola.setText(" Inventario y componentes gráficos completamente vacíos.");
    }

    private void updateScreen() {
        StringBuilder sb = new StringBuilder("--- Árbol Binario de Búsqueda (Recorrido In-Order) ---\n");
        if (productList.isEmpty()) {
            sb.append("[Inventario Vacío]");
        } else {
            for (Product p : productList) {
                sb.append(p.toString()).append("\n");
            }
        }
        txtAreaConsola.setText(sb.toString());
        drawBstGraph(-1);
    }

    // DIBUJO GEOMÉTRICO DINÁMICO EN CANVAS/PANE
    private void drawBstGraph(int highlightedId) {
        panelDibujo.getChildren().clear();
        if (productList.isEmpty()) return;

        // Construcción recursiva simulada en base al tamaño de la lista de nodos
        drawRecursiveNode(0, productList.size() - 1, panelDibujo.getPrefWidth() / 2, 40, panelDibujo.getPrefWidth() / 4, 1, highlightedId);
    }

    private void drawRecursiveNode(int start, int end, double x, double y, double xOffset, int level, int highlightedId) {
        if (start > end) return;

        int mid = (start + end) / 2;
        Product prod = productList.get(mid);

        double nextY = y + 60;

        // Dibujar Aristas/Líneas hacia hijos izquierdos y derechos
        if (start <= mid - 1) {
            double leftX = x - xOffset;
            Line leftLine = new Line(x, y, leftX, nextY);
            leftLine.setStroke(Color.web("#94a3b8"));
            leftLine.setStrokeWidth(2);
            panelDibujo.getChildren().add(leftLine);
            drawRecursiveNode(start, mid - 1, leftX, nextY, xOffset / 1.8, level + 1, highlightedId);
        }

        if (mid + 1 <= end) {
            double rightX = x + xOffset;
            Line rightLine = new Line(x, y, rightX, nextY);
            rightLine.setStroke(Color.web("#94a3b8"));
            rightLine.setStrokeWidth(2);
            panelDibujo.getChildren().add(rightLine);
            drawRecursiveNode(mid + 1, end, rightX, nextY, xOffset / 1.8, level + 1, highlightedId);
        }

        // Crear contenedor geométrico circular para el nodo
        Circle circle = new Circle(x, y, 18);
        if (prod.getId() == highlightedId) {
            circle.setFill(Color.web("#f59e0b")); // Amarillo si es el nodo encontrado en la búsqueda
        } else {
            circle.setFill(Color.web("#3b82f6"));  // Azul corporativo base
        }
        circle.setStroke(Color.web("#1d4ed8"));
        circle.setStrokeWidth(1.5);

        // Texto informativo interno (ID del producto)
        Text labelText = new Text(String.valueOf(prod.getId()));
        labelText.setFill(Color.WHITE);
        labelText.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        labelText.setX(x - 6);
        labelText.setY(y + 4);

        panelDibujo.getChildren().addAll(circle, labelText);
    }
}
