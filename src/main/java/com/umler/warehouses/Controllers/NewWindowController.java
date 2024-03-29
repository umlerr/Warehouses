package com.umler.warehouses.Controllers;

import com.umler.warehouses.Enums.ScenePath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * Контроллер переключения.
 * @author Umler
 */
public class NewWindowController {

    static double x;
    static double y;

    /**
     * Открывает новое окно для добавления нового стеллажа.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewShelfWindow() throws IOException {
        getPopUpWindow(ScenePath.ADDSHELF.getPath());
    }

    /**
     * Открывает новое окно для добавления нового помещения.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewRoomWindow() throws IOException {
        getPopUpWindow(ScenePath.ADDROOM.getPath());
    }

    /**
     * Открывает новое окно для добавления нового продукта.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewProductWindow() throws IOException {
        getPopUpWindow(ScenePath.ADDPRODUCT.getPath());
    }

    /**
     * Открывает новое окно для добавления нового контракта и компании.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getNewContractCompanyWindow() throws IOException {
        getPopUpWindow(ScenePath.ADDCONTRACTCOMPANY.getPath());
    }

    /**
     * Открывает новое всплывающее окно с заданным FXML-файлом.
     * @param path Путь к FXML-файлу в классе Enum ScenePath.
     * @throws IOException Если произошла ошибка ввода-вывода.
     */
    public static void getPopUpWindow(String path) throws IOException {
        Stage stage = new Stage();
        Pane main = FXMLLoader.load(Objects.requireNonNull(NewWindowController.class.getResource(path)));
        controlDrag(main, stage);
        stage.setScene(new Scene(main));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Popup window");
        stage.getScene();
        stage.showAndWait();
    }

    /**
     * Окно можно перетаскивать по экрану при зажатии ЛКМ по окну.
     */
    public static void controlDrag(Pane main, Stage stage) {
        main.setOnMousePressed(event -> {
            x = stage.getX() - event.getScreenX();
            y = stage.getY() - event.getScreenY();
        });
        main.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() + x);
            stage.setY(event.getScreenY() + y);
        });
    }

}
