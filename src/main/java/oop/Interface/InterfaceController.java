package oop.Interface;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InterfaceController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}