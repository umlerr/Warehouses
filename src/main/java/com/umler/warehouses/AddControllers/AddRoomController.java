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

    /**
     * Сохраняет изменения в базе данных после добавления комнаты.
     * Если данные введены корректно, то создается новый объект компании и контракта и происходит обновление базы данных.
     * Если данные введены некорректно, то выводится сообщение об ошибке.
     */
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

    /**
     * Проверяет корректность введенных данных.
     * Если все поля заполнены корректно, возвращает true.
     * Если есть незаполненные поля или данные введены некорректно, выводит сообщение об ошибке и возвращает false.
     */
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

    /**
     * Проверка что введеный номер помещения уже существует.
     */
    private boolean isNumberExist(Integer number){
        for (Room rooms : roomService.getRooms()){
            if (Objects.equals(rooms.getNumber(), number))
                return false;
        }
        return true;
    }

    /**
     * Создает новый объект помещения на основе введенных данных.
     * @return новый объект помещения
     */
    private Room createRoomFromInput() {
        Room room = new Room();
        room.setNumber(Integer.valueOf(number_field.getText()));
        room.setCapacity(Integer.valueOf(capacity_field.getText()));
        return room;
    }

    /**
     * Проверка введенной строки на число.
     * @return true - число больше 0/false
     */
    private boolean isNumeric(String str) {
        try {
            return Double.parseDouble(str) <= 0;
        } catch(NumberFormatException e){
            return true;
        }
    }

    /**
     * Задержка закрытия окна добавления до нажатия клавишы сохранения.
     */
    private void delayWindowClose(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event2 -> closeWindow(event));
        delay.play();
    }

    /**
     * Закрытия окна по кнопке.
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        SceneController.close(event);
    }

}
