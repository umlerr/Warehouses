package com.umler.warehouses.Converters;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.converter.IntegerStringConverter;

public class CustomIntegerStringConverter extends IntegerStringConverter {
    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {
            showAlert();
        }
        return null;
    }

    @Override
    public Integer fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            showAlert();
        }
        return -1;
    }

    private void showAlert(){
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        IOAlert.setContentText("Incorrect input, you need to input a number");
        IOAlert.showAndWait();
        if(IOAlert.getResult() == ButtonType.OK)
        {
            IOAlert.close();
        }
    }
}



