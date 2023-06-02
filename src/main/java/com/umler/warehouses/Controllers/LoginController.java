package com.umler.warehouses.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Model.User;
import com.umler.warehouses.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Initializable {

    public Button LoginButton;

    public Button exit_btn;

    public Button wrap_btn;

    @FXML
    private Label invalid_label;

    @FXML
    private TextField username_box;

    @FXML
    private TextField password_box;

    UserService userService = new UserService();

    private static final Logger logger = LoggerFactory.getLogger("LoginScene Logger");

    @FXML
    void showVisitScreen(ActionEvent event) throws IOException {
        if (validateLogin())
        {
            SceneController.getContractsScene(event);
        }
        else
        {

            logger.warn("Invalid credentials");

            username_box.clear();
            password_box.clear();
            invalid_label.setText("Invalid credentials");
        }

    }

    private boolean validateLogin() {
        User user = userService.getConnectedUser(username_box.getText(), password_box.getText());
        if (user == null) {
            return false;
        }
        CurrentUser.setCurrentUser(user);
        return true;
    }

    public void ExitLoginWindow() {
        logger.debug("Closing login window");
        exit_btn.setOnAction(SceneController::close);
    }

    public void WrapLoginWindow() {
        logger.debug("Wrapping login window");
        wrap_btn.setOnAction(SceneController::wrap);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}