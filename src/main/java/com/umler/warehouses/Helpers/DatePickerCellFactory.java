package com.umler.warehouses.Helpers;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.time.LocalDate;

/**
 * Класс для переопределения вида ячеек таблицы под DatePicker календарь.
 * @author Umler
 */

public class DatePickerCellFactory implements Callback<DatePicker, DateCell> {

    private final LocalDate selectedDate;

    public DatePickerCellFactory(DatePicker datePicker) {
        if (datePicker.getValue()==null)
        {
            selectedDate = LocalDate.now();
        }
        else {
            selectedDate = datePicker.getValue();
        }
        datePicker.setDayCellFactory(this);
    }

    @Override
    public DateCell call(DatePicker param) {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(selectedDate)) {
                    setDisable(true);
                    this.setStyle("-fx-background-color: #ffc5cf;");
                }
            }
        };
    }

    public LocalDate getValue() {
        return selectedDate;
    }
}