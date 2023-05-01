package com.umler.warehouses.main_interface_controllers;

import com.umler.warehouses.helpers.ScenePath;
import com.umler.warehouses.main_app.InterfaceController;
import com.umler.warehouses.main_app.Warehouse;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
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

    public static void getWarehousesScene(ActionEvent event) throws IOException {

        logger.debug("Transition to warehouses list scene");

        changeScreen(event, ScenePath.WAREHOUSES.getPath(),new Warehouse(-1,""));
    }

    public static void getMainScene(ActionEvent event, Warehouse warehouse) throws IOException {

        logger.debug("Transition to main window scene");

        changeScreen(event, ScenePath.HOME.getPath(), warehouse);
    }

    private static void changeScreen(ActionEvent event, String path, Warehouse warehouse) throws IOException
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

        if (path.equals(ScenePath.HOME.getPath()))
        {

            logger.debug("Making controller object to show required warehouse");

            InterfaceController controller = loader.getController();
            controller.toWarehouse(warehouse);
        }
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
