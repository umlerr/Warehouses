package com.umler.warehouses.Helpers;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Services.ContractService;

import java.time.LocalDate;
import java.sql.Date;



public class LocalDateCellFactory implements Callback<TableColumn<Contract, LocalDate>, TableCell<Contract, LocalDate>> {
    ContractService contractService = new ContractService();
    @Override
    public TableCell<Contract, LocalDate> call(TableColumn<Contract, LocalDate> col) {
        return new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.editableProperty().set(false);
                datePicker.setOnAction((e) -> {
                    commitEdit(datePicker.getValue());
                });
                this.setGraphic(datePicker);
            }

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                }
            }

            @Override
            public void commitEdit(LocalDate newValue) {
                super.commitEdit(newValue);
                Contract contract = getTableView().getColumns().get(0).getTableView().getItems().get(getIndex());
                contract.setStartDate(Date.valueOf(newValue).toLocalDate());
                contractService.updateContract(contract);
            }

            @Override
            public void startEdit() {
                super.startEdit();
                LocalDate value = getItem();
                if (value != null) {
                    datePicker.setValue(value);
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
            }
        };
    }
}
