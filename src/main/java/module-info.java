module sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires jdk.scripting.nashorn;


    opens oop.Interface to javafx.fxml;
    exports oop.Interface;
}