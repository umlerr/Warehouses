package com.umler.warehouses.AddControllers;

import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Room;
import com.umler.warehouses.Services.RoomService;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.Objects;


public class AddRoomController {

    @FXML
    public TextField number_field;
    @FXML
    public TextField capacity_field;

    RoomService roomService = new RoomService();

    @FXML
    private void saveNewRoomToDb(ActionEvent event){
        if (validateInputs()) {
            Room room = createRoomFromInput();

            boolean isSaved = new RoomService().createRoom(room);
            if (isSaved) {
                UpdateStatus.setIsRoomAdded(true);
                delayWindowClose(event);
            }
        }
    }

    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (number_field.getText().equals("")
                || capacity_field.getText().equals("")) {
            IOAlert.setContentText("You must fill out the room to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (!isNumberExist(Integer.valueOf(number_field.getText()))){
            IOAlert.setContentText("Incorrect input for ROOM NUMBER - A ROOM with this number already exists");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (isNumeric(number_field.getText())){
            IOAlert.setContentText("Incorrect input for room number - you must put a positive number");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (isNumeric(capacity_field.getText())){
            IOAlert.setContentText("Incorrect input for capacity - you must put a positive number");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        return true;
    }

    private boolean isNumberExist(Integer number){
        for (Room rooms : roomService.getRooms()){
            if (Objects.equals(rooms.getNumber(), number))
                return false;
        }
        return true;
    }

    private Room createRoomFromInput() {
        Room room = new Room();
        room.setNumber(Integer.valueOf(number_field.getText()));
        room.setCapacity(Integer.valueOf(capacity_field.getText()));
        return room;
    }

    private boolean isNumeric(String str) {
        try {
            return Double.parseDouble(str) <= 0;
        } catch(NumberFormatException e){
            return true;
        }
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
