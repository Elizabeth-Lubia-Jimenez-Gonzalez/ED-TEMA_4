package com.lubiasystem.menuestrnolineales.controller;

import com.lubiasystem.menuestrnolineales.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MenuController {

    @FXML
    private void abrirBst() {
        loadWindow("/com/lubiasystem/menuestrnolineales/bst-view.fxml", "Inventario - BST");
    }

    @FXML
    private void abrirOrganigrama() {
        loadWindow("/com/lubiasystem/menuestrnolineales/organigrama-view.fxml", "Organigrama - Árbol Jerárquico");
    }

    @FXML
    private void abrirGrafo() {
        loadWindow("/com/lubiasystem/menuestrnolineales/grafo-view.fxml", "Logística - Red de Grafos");
    }

    @FXML
    private void abrirUrgencias() {
        loadWindow("/com/lubiasystem/menuestrnolineales/urgencias-view.fxml", "Triage - Montículo");
    }

    private void loadWindow(String fxmlPath, String windowTitle) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
