module com.lubiasystem.logigrap {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.lubiasystem.logigrap to javafx.fxml;
    opens com.lubiasystem.logigrap.controller to javafx.fxml;

    exports com.lubiasystem.logigrap;
    exports com.lubiasystem.logigrap.controller;
    exports com.lubiasystem.logigrap.clases;
}