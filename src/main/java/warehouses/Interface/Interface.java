package warehouses.Interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Interface extends Application {

    @Override
    public void start(Stage MainStage) throws IOException {
        Parent LoginParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
        Scene LoginScene = new Scene(LoginParent);
        MainStage.setScene(LoginScene);
//        MainStage.initStyle(StageStyle.UTILITY);
        MainStage.setTitle("Login");
        MainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}