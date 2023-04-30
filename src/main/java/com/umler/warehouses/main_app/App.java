package com.umler.warehouses.main_app;

import com.umler.warehouses.main_interface_controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneController.getInitialScene(stage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
//        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

}