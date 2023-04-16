module sample {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;


    opens oop.Interface to javafx.fxml;
    exports oop.Interface;
}