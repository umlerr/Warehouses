module oop.Interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;

    opens warehouses.Interface to javafx.fxml;
    exports warehouses.Interface;
}