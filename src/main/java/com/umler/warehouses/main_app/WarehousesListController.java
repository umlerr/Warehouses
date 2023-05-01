package com.umler.warehouses.main_app;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.umler.warehouses.helpers.ScenePath;
import com.umler.warehouses.main_interface_controllers.SceneController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.umler.warehouses.main_interface_controllers.SceneController.getMainScene;


public class WarehousesListController extends InterfaceController implements Initializable {

    @FXML
    public TextField Table_Search;

    @FXML
    public TableColumn<Warehouse, String> ID_col;

    @FXML
    public TableColumn<Warehouse, String> Address_col;

    public Button exit_btn;

    public Button wrap_btn;

    @FXML
    private TableView<Warehouse> Table;

    private final ObservableList<Warehouse> WTable = FXCollections.observableArrayList();

    private static final Logger logger = LoggerFactory.getLogger("WarehousesList Logger");


    static class MyDeleteException extends Exception
    {
        public MyDeleteException()
        {
            super("Choose a row to delete");
        }
    }

    static class MyContinueException extends Exception
    {
        public MyContinueException()
        {
            super("Select a warehouse to continue");
        }
    }

    @FXML
    private void WTable_Add(ActionEvent event)
    {

        logger.debug("Adding new warehouse");

        Integer ID = 999;
        WTable.add(new Warehouse(ID,"-"));
        Table.setItems(WTable);
        WTable_Search();

        logger.info("New warehouse added");
    }

    @FXML
    private void WTable_Delete(ActionEvent event) {
        try {

            logger.debug("Deleting warehouse");

            Table.setItems(WTable);
            int selectedID = Table.getSelectionModel().getSelectedIndex();
            if (selectedID == -1) throw new MyDeleteException();
            else
            {
                Table.getItems().remove(selectedID);

                logger.info("Warehouse deleted");
            }
        }
        catch (MyDeleteException myEx)
        {

            logger.warn("MyDeleteException " + myEx);

            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText(myEx.getMessage());
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
        WTable_Search();
    }

    @FXML
    private void WTable_Search()
    {
        FilteredList<Warehouse> filteredData = new FilteredList<>(WTable, b -> true);
        Table_Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(warehouse ->
            {
                logger.debug("Searching");

                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(warehouse.getID()).contains(lowerCaseFilter) ||
                        warehouse.getAddress().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Warehouse> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(Table.comparatorProperty());
        Table.setItems(sortedData);
    }

    @FXML
    public void showMainScreen(ActionEvent event) throws IOException {
        try {
            int selectedID = Table.getSelectionModel().getSelectedIndex();
            if (selectedID == -1) throw new WarehousesListController.MyContinueException();
            else
            {

                logger.debug("Transition to main screen");

                getMainScene(event,Table.getSelectionModel().getSelectedItem());
            }
        }
        catch (MyContinueException myEx)
        {

            logger.warn("MyContinueException " + myEx);

            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText(myEx.getMessage());
            IOAlert.showAndWait();
            if (IOAlert.getResult() == ButtonType.OK) {
                IOAlert.close();
            }
        }
    }

    public void WTableEdit1(TableColumn.CellEditEvent<Warehouse, String> userStringCellEditEvent)
    {
        Warehouse warehouse = Table.getSelectionModel().getSelectedItem();
        warehouse.setID(Integer.valueOf(userStringCellEditEvent.getNewValue()));

        logger.debug("Editing cell");

        WTable_Search();
    }
    public void WTableEdit2(TableColumn.CellEditEvent<Warehouse, String> userStringCellEditEvent)
    {
        Warehouse warehouse = Table.getSelectionModel().getSelectedItem();
        warehouse.setAddress(userStringCellEditEvent.getNewValue());

        logger.debug("Editing cell");

        WTable_Search();
    }

    public void ExitWListWindow() {

        logger.debug("Closing WList window");

        exit_btn.setOnAction(SceneController::close);
    }

    public void WrapWListWindow() {

        logger.debug("Wrapping WList window");

        wrap_btn.setOnAction(SceneController::wrap);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        WTable.add(new Warehouse(1,"ул. Профессора Попова, дом 5 литера Ф, Санкт-Петербург, Россия, 197022"));
        WTable.add(new Warehouse(3,"ул. Говна"));
        Table.setItems(WTable);

        ID_col.setCellValueFactory(new PropertyValueFactory<>("ID"));
        Address_col.setCellValueFactory(new PropertyValueFactory<>("Address"));

        Table.setEditable(true);

        Address_col.setCellFactory(TextFieldTableCell.forTableColumn());

        WTable_Search();
    }
}
