package oop.Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Login extends Application {

    private Stage LoginStage;
    private AnchorPane LoginLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage LoginStage) throws IOException {
        this.LoginStage = LoginStage;
        this.LoginStage.setTitle("Login");

        showLogin();

    }

    public void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Interface.class.getResource("login.fxml"));
        LoginLayout = loader.load();
        Scene scene = new Scene(LoginLayout);
        LoginStage.setScene(scene);
        LoginStage.show();
    }
}
