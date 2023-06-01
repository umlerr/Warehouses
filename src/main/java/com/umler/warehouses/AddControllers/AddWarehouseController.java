package com.umler.warehouses.AddControllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Warehouse;
import com.umler.warehouses.Services.WarehouseService;


public class AddWarehouseController {

    @FXML
    public TextField house_field;
    @FXML
    public TextField city_field;
    @FXML
    public TextField index_field;
    @FXML
    public TextField street_field;


    @FXML
    private void saveNewWarehouseToDb(ActionEvent event){
        if (validateInputs()) {
            Warehouse warehouse = createWarehouseFromInput();
            boolean isSaved = new WarehouseService().createWarehouse(warehouse);
            if (isSaved) {
                UpdateStatus.setIsWarehouseAdded(true);
                delayWindowClose(event);
            }
        }
    }

    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (city_field.getText().equals("") || street_field.getText().equals("") || house_field.getText().equals("") ||
                index_field.getText().equals("")) {
            IOAlert.setContentText("You must enter the address to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        return true;
    }

    private Warehouse createWarehouseFromInput() {
        Warehouse warehouse = new Warehouse();
        warehouse.setAddress(capitalize(city_field.getText().toLowerCase()) + ", st." +
                capitalize(street_field.getText().toLowerCase()) + ", " +
                house_field.getText() +
                ", " + index_field.getText());
        return warehouse;
    }

    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private void delayWindowClose(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event2 -> closeWindow(event));
        delay.play();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        SceneController.close(event);
    }
}
