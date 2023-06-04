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
 * Code created by Umler on 2023-04-30
 */
public class SceneController {

    private static double x;
    private static double y;

    private static Parent main;

    private static final Logger logger = LoggerFactory.getLogger("Scene Logger");

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

    public static void getCompaniesScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Companies scene");

        changeScreen(event, ScenePath.COMPANIES.getPath());
    }

    public static void getProductsScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Products scene");

        changeScreen(event, ScenePath.PRODUCTS.getPath());
    }

    public static void getRoomsShelvesScene(ActionEvent event) throws IOException {

        logger.debug("Transition to RoomsShelves scene");

        changeScreen(event, ScenePath.ROOMSSHELVES.getPath());
    }

    public static void getContractsScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Contracts scene");
        changeScreen(event, ScenePath.CONTRACTS.getPath());
    }

    public static void getLoginScene(ActionEvent event) throws IOException {

        logger.debug("Transition to Login scene");

        changeScreen(event, ScenePath.LOGIN.getPath());
    }


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
    public static void close(ActionEvent actionEvent) {

        logger.debug("Closing window");

        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public static void wrap(ActionEvent actionEvent) {

        logger.debug("Wrapping window");

        Node  source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.setIconified(true);
    }

}
