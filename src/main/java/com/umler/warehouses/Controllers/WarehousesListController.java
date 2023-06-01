package com.umler.warehouses.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Model.Warehouse;
import com.umler.warehouses.Services.WarehouseService;
import com.umler.warehouses.Converters.CustomIntegerStringConverter;
import com.umler.warehouses.Helpers.UpdateStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.umler.warehouses.Controllers.SceneController.getMainScene;


public class WarehousesListController extends InterfaceController implements Initializable {

    @FXML
    public TextField Table_Search;

    @FXML
    public TableColumn<Warehouse, Integer> ID_col;

    @FXML
    public TableColumn<Warehouse, String> Address_col;

    @FXML
    public Hyperlink current_user;

    @FXML
    private TableView<Warehouse> Table;

    public Button exit_btn;

    public Button wrap_btn;


    ObservableList<Warehouse> WList = FXCollections.observableArrayList();

    WarehouseService warehouseService = new WarehouseService();

    private static final Logger logger = LoggerFactory.getLogger("WarehousesList Logger");


    static class myDeleteException extends Exception
    {
        public myDeleteException()
        {
            super("Choose a row to delete");
        }
    }

    static class myContinueException extends Exception
    {
        public myContinueException()
        {
            super("Select a warehouse to continue");
        }
    }




    private void setWList() {
        WList.clear();
        WList.addAll(warehouseService.getWarehouses());
    }

    private ObservableList<Warehouse> getSortedList() {
        SortedList<Warehouse> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(Table.comparatorProperty());
        return sortedList;
    }

    private FilteredList<Warehouse> getFilteredList() {
        FilteredList<Warehouse> filteredList = new FilteredList<>(WList, b -> true);
        Table_Search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(warehouse -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(warehouse.getId_warehouse()).contains(lowerCaseFilter)) {
                        return true;
                    } else return warehouse.getAddress().toLowerCase().contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    @FXML
    private void WListAdd(ActionEvent event) throws IOException {

        logger.debug("Adding new warehouse");
        NewWindowController.getNewWarehouseWindow();

        if(UpdateStatus.isIsWarehouseAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsWarehouseAdded(false);
        }

        logger.info("New warehouse added");
    }

    @FXML
    private void WListDelete(ActionEvent event) {
        try {

            logger.debug("deleting a worker");
            deleteRows(event);
            logger.info("worker deleted");
        }

        catch (myDeleteException | IOException myEx){

            logger.error("MyDeleteException " + myEx);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText(myEx.getMessage());
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    private void deleteRows(ActionEvent event) throws myDeleteException, IOException {

        int selectedID = Table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new myDeleteException();
        else {
            ObservableList<Warehouse> selectedRows = Table.getSelectionModel().getSelectedItems();
            for (Warehouse worker : selectedRows) {
                warehouseService.deleteWarehouse(worker);
            }
            refreshScreen(event);
        }
    }

    @FXML
    public void WListEditID(TableColumn.CellEditEvent<Warehouse, Integer> EditEvent)
    {
        Warehouse selectedWarehouse = Table.getSelectionModel().getSelectedItem();
        Integer value = EditEvent.getNewValue();
        if (value >= 0) {
            selectedWarehouse.setId_warehouse(value);
            warehouseService.updateWarehouse(selectedWarehouse);
        }
        else Table.refresh();

        logger.debug("Editing cell");
    }

    public void WListEditAddress(TableColumn.CellEditEvent<Warehouse, String> EditEvent)
    {
        Warehouse warehouse = Table.getSelectionModel().getSelectedItem();
        warehouse.setAddress(EditEvent.getNewValue());

        logger.debug("Editing cell");
    }

    @FXML
    public void showMainScreen(ActionEvent event) throws IOException {
        try {
            int selectedID = Table.getSelectionModel().getSelectedIndex();
            if (selectedID == -1) throw new WarehousesListController.myContinueException();
            else
            {

                logger.debug("Transition to main screen");

                getMainScene(event,Table.getSelectionModel().getSelectedItem());
            }
        }
        catch (myContinueException myEx)
        {

            logger.error("MyContinueException " + myEx);

            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText(myEx.getMessage());
            IOAlert.showAndWait();
            if (IOAlert.getResult() == ButtonType.OK) {
                IOAlert.close();
            }
        }
    }

    @FXML
    public void changeUser(ActionEvent event) throws IOException {
        SceneController.getLoginScene(event);
    }

    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getWarehousesScene(event);
    }

    public void exitWListWindow() {

        logger.debug("Closing WList window");

        exit_btn.setOnAction(SceneController::close);
    }

    public void wrapWListWindow() {

        logger.debug("Wrapping WList window");

        wrap_btn.setOnAction(SceneController::wrap);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        current_user.setText(CurrentUser.getCurrentUser().getName() + " / Change");
        Table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setWList();

        ID_col.setCellValueFactory(new PropertyValueFactory<>("Id_warehouse"));
        Address_col.setCellValueFactory(new PropertyValueFactory<>("Address"));

        Table.setEditable(true);

        Address_col.setCellFactory(TextFieldTableCell.forTableColumn());
        ID_col.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));

        Table.setItems(getSortedList());
    }
}
