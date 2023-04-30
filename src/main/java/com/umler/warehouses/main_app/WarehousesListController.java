package com.umler.warehouses.main_app;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.umler.warehouses.helpers.ScenePath;
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


public class WarehousesListController extends InterfaceController implements Initializable {

    @FXML
    public TextField Table_Search;

    @FXML
    public TableColumn<Warehouse, String> ID_col;

    @FXML
    public TableColumn<Warehouse, String> Address_col;

    @FXML
    private TableView<Warehouse> Table;

    @FXML
    private Label InvalidLabelContinue; //Select a warehouse to continue

    @FXML
    private Label InvalidLabelDelete;


    private final ObservableList<Warehouse> WTable = FXCollections.observableArrayList();

    static class MyExceptionDelete extends Exception
    {
        public MyExceptionDelete()
        {
            super("Choose a row to delete");
        }
    }

    static class MyExceptionContinue extends Exception
    {
        public MyExceptionContinue()
        {
            super("Select a warehouse to continue");
        }
    }

    @FXML
    private void WTable_Add(ActionEvent event)
    {
        Integer ID = 999;
        WTable.add(new Warehouse(ID,"-"));
        Table.setItems(WTable);
        WTable_Search();
    }

    @FXML
    private void WTable_Delete(ActionEvent event) {
        try {
            InvalidLabelDelete.setText("");
            Table.setItems(WTable);
            int selectedID = Table.getSelectionModel().getSelectedIndex();
            if (selectedID == -1) throw new MyExceptionDelete();
            else Table.getItems().remove(selectedID);
        }
        catch (MyExceptionDelete myEx){
            InvalidLabelDelete.setText("Choose a row to delete");
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

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ScenePath.HOME.getPath()));
        Parent MainParent = loader.load();

        Scene MainScene = new Scene(MainParent);

        InterfaceController controller = loader.getController();
        Stage MainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        MainStage.setTitle("Warehouses");

        try {
            int selectedID = Table.getSelectionModel().getSelectedIndex();
            if (selectedID == -1) throw new WarehousesListController.MyExceptionContinue();
            else
            {
                controller.toWarehouse(Table.getSelectionModel().getSelectedItem());
                MainStage.setScene(MainScene);
                MainStage.show();
            }
        }
        catch (MyExceptionContinue myEx)
        {
            InvalidLabelDelete.setText("Select a warehouse to continue");
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
        WTable_Search();
    }
    public void WTableEdit2(TableColumn.CellEditEvent<Warehouse, String> userStringCellEditEvent)
    {
        Warehouse warehouse = Table.getSelectionModel().getSelectedItem();
        warehouse.setAddress(userStringCellEditEvent.getNewValue());
        WTable_Search();
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
