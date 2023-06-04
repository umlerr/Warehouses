package com.umler.warehouses.AddControllers;

import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Shelf;
import com.umler.warehouses.Model.Room;
import com.umler.warehouses.Services.ShelfService;
import com.umler.warehouses.Services.RoomService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddShelfController implements Initializable {

    @FXML
    public TextField number_field;
    @FXML
    public TextField capacity_field;
    @FXML
    public ChoiceBox<Room> room_choicebox;

    RoomService roomService = new RoomService();

    ShelfService shelfService = new ShelfService();


    @FXML
    private void saveNewShelfToDb(ActionEvent event){
        if (validateInputs()) {
            Shelf shelf = createShelfFromInput();

            boolean isSaved = new ShelfService().createShelf(shelf);
            if (isSaved) {
                UpdateStatus.setIsShelfAdded(true);
                delayWindowClose(event);
            }
        }
    }

    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (number_field.getText().equals("")
                || capacity_field.getText().equals("")
                || room_choicebox.getValue() == null) {
            IOAlert.setContentText("You must fill out the shelf to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (!isNumberExist(Integer.valueOf(number_field.getText()))){
            IOAlert.setContentText("Incorrect input for SHELF NUMBER - SHELF with this number already exists");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (isNumeric(number_field.getText())){
            IOAlert.setContentText("Incorrect input for shelf number - you must put a positive number");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if(!isRoomFree(room_choicebox.getValue().getNumber()))
        {
            IOAlert.setContentText("Not enough space in room to add new shelf!");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        return true;
    }

    private Shelf createShelfFromInput() {
        Shelf shelf = new Shelf();
        shelf.setNumber(Integer.valueOf(number_field.getText().toLowerCase()));
        shelf.setCapacity(Integer.valueOf(capacity_field.getText().toLowerCase()));
        shelf.setRoom(room_choicebox.getValue());
        return shelf;
    }

    private boolean isNumberExist(Integer number){
        for (Shelf shelf : shelfService.getShelves()){
            if (Objects.equals(shelf.getNumber(), number))
                return false;
        }
        return true;
    }

    private boolean isRoomFree(Integer number){
        Room room = roomService.getRoom(number);
        List<Shelf> shelves = room.getShelvesList();
        return shelves.size() + 1 <= room.getCapacity();
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

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ObservableList<Room> roomObservableList = FXCollections.observableArrayList(roomService.getRooms());
        room_choicebox.getItems().addAll(roomObservableList);
    }
}
