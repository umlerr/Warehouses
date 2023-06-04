package com.umler.warehouses.Helpers;

import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Services.ContractService;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Date;
import java.time.LocalDate;

public class LocalStartDateCellFactory implements Callback<TableColumn<Contract, LocalDate>, TableCell<Contract, LocalDate>> {
    ContractService contractService = new ContractService();
    @Override
    public TableCell<Contract, LocalDate> call(TableColumn<Contract, LocalDate> col) {
        return new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();
            private LocalDate maxDate = LocalDate.of(9999,12,1);

            {
                datePicker.editableProperty().set(false);
                datePicker.setOnAction((e) -> commitEdit(datePicker.getValue()));
                this.setGraphic(datePicker);

                // задаем диапазон дат для выбора значения EndDate
                Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                // если дата раньше minDate, то недоступна для выбора
                                if (item != null && item.isAfter(maxDate)) {
                                    this.setDisable(true);
                                    this.setStyle("-fx-background-color: #ffc5cf;"); // также помечаем цветом
                                }
                            }
                        };
                    }
                };
                datePicker.setDayCellFactory(dayCellFactory);
            }

            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                Contract contract = getTableRow().getItem();
                if (item == null || empty || contract == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                    maxDate = contract.getEnddate().minusDays(1); // Устанавливаем minDate для этой ячейки
                }
            }

            @Override
            public void commitEdit(LocalDate newValue) {
                super.commitEdit(newValue);
                Contract contract = getTableView().getColumns().get(0).getTableView().getItems().get(getIndex());
                contract.setStartdate(Date.valueOf(newValue).toLocalDate());
                contractService.updateContract(contract);
            }

            @Override
            public void startEdit() {
                super.startEdit();
                LocalDate value = getItem();
                if (value != null) {
                    datePicker.setValue(value);
                }
                // задаем максимальную дату выбора EndDate
                Contract contract = getTableView().getColumns().get(0).getTableView().getItems().get(getIndex());
                LocalDate endDate = contract.getStartdate();
                maxDate = endDate.minusDays(1);
                Callback<DatePicker, DateCell> dayCellFactory = datePicker.getDayCellFactory();
                datePicker.setDayCellFactory(param -> {
                    DateCell cell = dayCellFactory.call(param);
                    if (cell.getItem().isAfter(maxDate)) {
                        cell.setDisable(true);
                    }
                    return cell;
                });
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
            }
        };
    }
}