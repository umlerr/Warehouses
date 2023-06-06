package com.umler.warehouses;

import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.HibernateUtil;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

import org.slf4j.*;

/**
 * Стартовый класс для запуска приложения
 * @author Umler
 */

public class App extends Application {

    private static final Logger logger = LoggerFactory.getLogger("Main Logger");

    /**
     * Начало программы
     */
    @Override
    public void start(Stage stage) throws IOException {
        SceneController.getInitialScene(stage);
        logger.debug("Start of a program");
    }

    /**
     * Конец программы
     */
    @Override
    public void stop() throws Exception {
        logger.debug("End of a program");
        super.stop();
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}