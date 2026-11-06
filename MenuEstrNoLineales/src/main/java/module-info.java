module com.lubiasystem.menuestrnolineales {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.lubiasystem.menuestrnolineales;
    opens com.lubiasystem.menuestrnolineales to javafx.fxml;

    exports com.lubiasystem.menuestrnolineales.controller;
    opens com.lubiasystem.menuestrnolineales.controller to javafx.fxml;

    exports com.lubiasystem.menuestrnolineales.clases;
    opens com.lubiasystem.menuestrnolineales.clases to javafx.base;
}