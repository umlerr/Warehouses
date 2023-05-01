package com.umler.warehouses.main_app;

import com.umler.warehouses.main_interface_controllers.SceneController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import org.slf4j.*;

public class App extends Application {

    private static final Logger logger = LoggerFactory.getLogger("Main Logger");

    @Override
    public void start(Stage stage) throws IOException {
        SceneController.getInitialScene(stage);
        logger.debug("Start of a program");
    }

    @Override
    public void stop() throws Exception {
        logger.debug("End of a program");
        super.stop();
//        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }

}