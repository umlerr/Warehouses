module oop.Interface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires itextpdf;
    requires org.hibernate.orm.core;
    requires slf4j.api;

    opens com.umler.warehouses.main_app to javafx.fxml;
    exports com.umler.warehouses.main_app;
}