package com.umler.warehouses.main_app;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import com.umler.warehouses.main_interface_controllers.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

    public Button LoginButton;
    @FXML
    private Label invalid_label;

    @FXML
    private TextField username_box;

    @FXML
    private TextField password_box;

    @FXML
    void showVisitScreen(ActionEvent event) throws IOException {
        if (isValidCredentials())
        {
            SceneController.getWarehousesScene(event);
        }
        else
        {
            username_box.clear();
            password_box.clear();
            invalid_label.setText("Sorry, invalid credentials");
        }

    }

    private boolean isValidCredentials()
    {
        boolean LetIn = false;
        Connection Connection = null;
        java.sql.Statement Statement = null;
        try {
            Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LoginDB", "Umler", "Umler1337228");
            Connection.setAutoCommit(false);
            Statement = Connection.createStatement();

            ResultSet Result = Statement.executeQuery( "SELECT * FROM Users WHERE UserName= " + "'" + username_box.getText() + "'"
                    + " AND Password= " + "'" + password_box.getText() + "'");

            while ( Result.next() ) {
                if (Result.getString("UserName") != null && Result.getString("Password") != null) {
                    LetIn = true;
                }
            }
            Result.close();
            Statement.close();
            Connection.close();
        }
        catch ( Exception e )
        {
            System.exit(0);
        }
        return LetIn;

    }

    private void close() {
//        exitBtn.setOnAction(SceneController::close);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}