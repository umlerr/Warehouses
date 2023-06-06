package com.umler.warehouses.Controllers;

import com.umler.warehouses.Enums.ScenePath;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Контроллер для переключения сцен интерфейса.
 * @author Umler
 */
public class SceneController {

    private static double x;
    private static double y;

    private static Parent main;

    private static final Logger logger = LoggerFactory.getLogger("Scene Logger");

    /**
     * Метод для перехода на сцену с авторизицией / Создание главной сцены.
     * @param stage Главная сцена.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    public static void getInitialScene(Stage stage) throws IOException {

        logger.debug("InitialScene");

        main = FXMLLoader.load((Objects.requireNonNull(SceneController.class.getResource(ScenePath.LOGIN.getPath()))));
        Scene scene = new Scene(main);


        controlDrag(stage);
        logger.debug("Draggable scene made");

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("WAREHOUSES");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Метод для перехода на сцену со списком компаний.
     * @param event Событие, вызвавшее переход.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    public static void getCompaniesScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Companies scene");

        changeScreen(event, ScenePath.COMPANIES.getPath());
    }

    /**
     * Метод для перехода на сцену со списком товаров.
     * @param event Событие, вызвавшее переход.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    public static void getProductsScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Products scene");

        changeScreen(event, ScenePath.PRODUCTS.getPath());
    }

    /**
     * Метод для перехода на сцену со списком помещений и стеллажей.
     * @param event Событие, вызвавшее переход.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    public static void getRoomsShelvesScene(ActionEvent event) throws IOException {

        logger.debug("Transition to RoomsShelves scene");

        changeScreen(event, ScenePath.ROOMSSHELVES.getPath());
    }

    /**
     * Метод для перехода на сцену со списком контрактов.
     * @param event Событие, вызвавшее переход.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    public static void getContractsScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Contracts scene");
        changeScreen(event, ScenePath.CONTRACTS.getPath());
    }

    /**
     * Метод для перехода на сцену с авторизацией.
     * @param event Событие, вызвавшее переход.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    public static void getLoginScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Login scene");

        changeScreen(event, ScenePath.LOGIN.getPath());
    }

    /**
     * Метод для смены сцены на заданную.
     * @param event Событие, вызвавшее смену сцены.
     * @param path Путь к файлу FXML новой сцены из класса Enum ScenePath.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    private static void changeScreen(ActionEvent event, String path) throws IOException
    {
        logger.debug("Changing scene");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Objects.requireNonNull(SceneController.class.getResource(path)));

        main = loader.load();
        Scene visitScene = new Scene(main);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        controlDrag(window);
        window.setScene(visitScene);
        window.show();
    }

    /**
     * Окно можно перетаскивать по экрану при зажатии ЛКМ по окну.
     */
    public static void controlDrag(Stage stage) {
        main.setOnMousePressed(event -> {
            x = stage.getX() - event.getScreenX();
            y = stage.getY() - event.getScreenY();
        });
        main.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + x);
            stage.setY(event.getScreenY() + y);
        });
    }

    /**
     * Метод для закрытия окон по нажатию кнопки.
     */
    public static void close(ActionEvent actionEvent) {

        logger.debug("Closing window");

        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Метод для сворачивания окон по нажатию кнопки.
     */
    public static void wrap(ActionEvent actionEvent) {

        logger.debug("Wrapping window");

        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

}
