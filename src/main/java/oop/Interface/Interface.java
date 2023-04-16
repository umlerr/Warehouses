package oop.Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Interface extends Application {
    private Stage MainStage;
    private BorderPane MainLayout;
    private Stage LoginStage;
    private AnchorPane LoginLayout;

    @Override
    public void start(Stage MainStage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Interface.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();


        this.MainStage = MainStage;
        this.MainStage.setTitle("Hotel");

        showMainView();
        showMainItems();
    }

//    public void start1(Stage LoginStage) throws IOException {
//        this.LoginStage = LoginStage;
//        this.LoginStage.setTitle("Login");
//
//        showLogin();
//    }

    private void showMainItems() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Interface.class.getResource("MainItems.fxml"));
        BorderPane MainItems = loader.load();
        MainLayout.setCenter(MainItems);
    }

    private void showMainView() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Interface.class.getResource("hello-view.fxml"));
        MainLayout = loader.load();
        Scene scene = new Scene(MainLayout);
        MainStage.setScene(scene);
        MainStage.show();
    }

    public void showLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Interface.class.getResource("login.fxml"));
        LoginLayout = loader.load();
        Scene scene = new Scene(LoginLayout);
        LoginStage.setScene(scene);
        LoginStage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}