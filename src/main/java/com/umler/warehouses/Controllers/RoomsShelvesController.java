package com.umler.warehouses.Controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Room;
import com.umler.warehouses.Model.Shelf;
import com.umler.warehouses.Services.RoomService;
import com.umler.warehouses.Services.ShelfService;
import com.umler.warehouses.Services.WarehouseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RoomsShelvesController implements Initializable
{
    @FXML
    public Button exit_btn;
    @FXML
    public Button wrap_btn;
    @FXML
    public TableColumn<Shelf, Integer> capacity_column;
    @FXML
    public TableColumn<Shelf, Integer> number_column;
    @FXML
    public Label search_invalid_label11;
    @FXML
    public Label search_invalid_label111;
    @FXML
    public Button refresh_btn;
    @FXML
    private TableColumn<Shelf, Room> room_column;
    @FXML
    private ChoiceBox<String> choice_box;
    @FXML
    private TableView<Shelf> table_shelves;
    @FXML
    public TableView<Room> table_rooms;
    @FXML
    public TableColumn<Room, Integer> roomnumber_column;
    @FXML
    public TableColumn<Room, Integer> roomcapacity_column;
    @FXML
    private TextField searchRoom;
    @FXML
    private TextField searchShelves;

    @FXML
    public Label fullness_label;

    WarehouseService warehouseService = new WarehouseService();

    ObservableList<Shelf> ShelfList = FXCollections.observableArrayList();

    ObservableList<Room> RoomList = FXCollections.observableArrayList();

    ShelfService shelfService = new ShelfService();

    RoomService roomService = new RoomService();


    private static final Logger logger = LoggerFactory.getLogger("Warehouse Logger");


    static class MyPDFException extends Exception
    {
        public MyPDFException()
        {
            super("There is nothing to save");
        }
    }

    static class myDeleteException extends Exception
    {
        public myDeleteException()
        {
            super("Choose a row to delete");
        }
    }

    private final String[] choices = {"Rooms/Shelves","Products","Companies", "Contracts"};

    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        logger.info("Choice box action");

        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Companies"))
        {
            logger.info("Choice box Companies selected");
            SceneController.getCompaniesScene(event);
        }
        if (Objects.equals(choice, "Contracts"))
        {
            logger.info("Choice box Contracts selected");
            SceneController.getContractsScene(event);
        }
        if (Objects.equals(choice, "Products"))
        {
            logger.info("Choice box Products selected");
            SceneController.getProductsScene(event);
        }
    }

    public void setRoomList() {
        RoomList.clear();
        RoomList.addAll(roomService.getRooms());
    }

    private ObservableList<Room> getRoomsSortedList() {
        SortedList<Room> sortedList = new SortedList<>(getRoomsFilteredList());
        sortedList.comparatorProperty().bind(table_rooms.comparatorProperty());
        return sortedList;
    }

    private FilteredList<Room> getRoomsFilteredList() {
        FilteredList<Room> filteredList = new FilteredList<>(RoomList, b -> true);
        searchRoom.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(room -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(room.getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return (String.valueOf(room.getCapacity()).toLowerCase().contains(lowerCaseFilter));
                }));
        return filteredList;
    }

    @FXML
    private void addRoom(ActionEvent event) throws IOException {
        logger.debug("adding a room");

        NewWindowController.getNewRoomWindow();
        if(UpdateStatus.isIsRoomAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsRoomAdded(false);
        }
        logger.info("room added");
    }

    @FXML
    private void deleteRooms(ActionEvent event)
    {
        try {
            logger.debug("deleting a shelf");
            deleteRoomRows(event);
            logger.info("shelf deleted");
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
    private void deleteRoomRows(ActionEvent event) throws myDeleteException, IOException {
        int selectedID = table_rooms.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new myDeleteException();
        else {
            ObservableList<Room> selectedRows = table_rooms.getSelectionModel().getSelectedItems();
            for (Room room : selectedRows) {
                roomService.deleteRoom(room);
            }
            refreshScreen(event);
        }
    }

    @FXML
    private void saveRooms()
    {
        try
        {
//            logger.debug("saving to file");
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_rooms.csv"));
            for(Room room : RoomList)
            {
                writer.write(room.getNumber() + ";" + room.getCapacity());
                writer.newLine();
            }
            writer.close();
            Desktop.getDesktop().open(new File("saves"));
//            logger.info("saved to file");
        }
        catch (IOException e)
        {
//            logger.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    @FXML
    public void editRoomNumber(TableColumn.CellEditEvent<Room, Integer> editEvent)
    {
        Room selectedRoom = table_rooms.getSelectionModel().getSelectedItem();
        selectedRoom.setNumber(editEvent.getNewValue());
        roomService.updateRoom(selectedRoom);

        logger.debug("Editing cell");
    }

    @FXML
    public void editRoomCapacity(TableColumn.CellEditEvent<Room, Integer> editEvent)
    {
        Room selectedRoom = table_rooms.getSelectionModel().getSelectedItem();
        selectedRoom.setCapacity(editEvent.getNewValue());
        roomService.updateRoom(selectedRoom);

        logger.debug("Editing cell");
    }

    @FXML
    public void toPDFRooms(ActionEvent event) throws IOException {
        try {
            Document my_pdf_report1 = new Document();
            PdfWriter.getInstance(my_pdf_report1, new FileOutputStream("report_rooms.pdf"));
            my_pdf_report1.open();

            PdfPTable my_report_table_rooms = new PdfPTable(2);

            PdfPCell table_rooms_cell;
            my_report_table_rooms.setHeaderRows(2);

            my_report_table_rooms.addCell(new PdfPCell(new Phrase("Number", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table_rooms.addCell(new PdfPCell(new Phrase("Capacity", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (RoomList.isEmpty()) throw new MyPDFException();

            for(Room rooms : RoomList)
            {
                table_rooms_cell=new PdfPCell(new Phrase(String.valueOf(rooms.getNumber())));
                my_report_table_rooms.addCell(table_rooms_cell);
                table_rooms_cell=new PdfPCell(new Phrase(String.valueOf(rooms.getCapacity())));
                my_report_table_rooms.addCell(table_rooms_cell);
            }
            my_pdf_report1.add(my_report_table_rooms);
            my_pdf_report1.close();

        }
        catch (FileNotFoundException | DocumentException | MyPDFException e)
        {
//            logger.warn("Exception " + e);
            e.printStackTrace();
        }
        refreshScreen(event);
    }

    public void setShelfList() {
        ShelfList.clear();
        ShelfList.addAll(shelfService.getShelves());
    }

    private ObservableList<Shelf> getShelvesSortedList() {
        SortedList<Shelf> sortedList = new SortedList<>(getShelvesFilteredList());
        sortedList.comparatorProperty().bind(table_shelves.comparatorProperty());
        return sortedList;
    }

    private FilteredList<Shelf> getShelvesFilteredList() {
        FilteredList<Shelf> filteredList = new FilteredList<>(ShelfList, b -> true);
        searchShelves.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(shelf -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (String.valueOf(shelf.getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(shelf.getCapacity()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(shelf.getRoom().getNumber()).toLowerCase().contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    @FXML
    private void addShelves(ActionEvent event) throws IOException {
        logger.debug("adding a shelf");

        NewWindowController.getNewShelfWindow();
        if(UpdateStatus.isIsShelfAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsShelfAdded(false);
        }
        logger.info("room shelf");
    }

    @FXML
    private void deleteShelves(ActionEvent event)
    {
        try {
            logger.debug("deleting a shelf");
            deleteShelveRows(event);
            logger.info("shelf deleted");
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
    private void deleteShelveRows(ActionEvent event) throws myDeleteException, IOException {

        int selectedID = table_shelves.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new myDeleteException();
        else {
            ObservableList<Shelf> selectedRows = table_shelves.getSelectionModel().getSelectedItems();
            for (Shelf shelf : selectedRows) {
                shelfService.deleteShelf(shelf);
            }
            refreshScreen(event);
        }
    }
    @FXML
    private void saveShelves()
    {
        try
        {
//            logger.debug("saving to file");
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_shelf.csv"));
            for(Shelf shelves : ShelfList)
            {
                writer.write(shelves.getNumber() + ";" + shelves.getCapacity() + ";"
                        + shelves.getRoom().getNumber());
                writer.newLine();
            }
            writer.close();
            Desktop.getDesktop().open(new File("saves"));
//            logger.info("saved to file");
        }
        catch (IOException e)
        {
//            logger.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }
    @FXML
    public void editShelfNumber(TableColumn.CellEditEvent<Shelf, Integer> editEvent)
    {
        Shelf selectedShelf = table_shelves.getSelectionModel().getSelectedItem();
        selectedShelf.setNumber(editEvent.getNewValue());
        shelfService.updateShelf(selectedShelf);

        logger.debug("Editing cell");
    }

    @FXML
    public void editShelfCapacity(TableColumn.CellEditEvent<Shelf, Integer> editEvent)
    {
        Shelf selectedShelf = table_shelves.getSelectionModel().getSelectedItem();
        selectedShelf.setCapacity(editEvent.getNewValue());
        shelfService.updateShelf(selectedShelf);

        logger.debug("Editing cell");
    }

    @FXML
    public void editRoom(TableColumn.CellEditEvent<Shelf, Room> editEvent)
    {
        Shelf selectedShelf = table_shelves.getSelectionModel().getSelectedItem();
        Room room = editEvent.getNewValue();
        selectedShelf.setRoom(room);
        shelfService.updateShelf(selectedShelf);

        logger.debug("Editing cell");
    }

    @FXML
    public void toPDFShelves(ActionEvent event) throws IOException {
        try {
//            logger.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report_shelfs.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table_shelves = new PdfPTable(3);

            PdfPCell table_shelves_cell;
            my_report_table_shelves.setHeaderRows(1);

            my_report_table_shelves.addCell(new PdfPCell(new Phrase("Number", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table_shelves.addCell(new PdfPCell(new Phrase("Capacity", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table_shelves.addCell(new PdfPCell(new Phrase("Room", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (ShelfList.isEmpty()) throw new MyPDFException();

            for(Shelf shelfs : ShelfList)
            {
                table_shelves_cell=new PdfPCell(new Phrase(String.valueOf(shelfs.getNumber())));
                my_report_table_shelves.addCell(table_shelves_cell);
                table_shelves_cell=new PdfPCell(new Phrase(String.valueOf(shelfs.getCapacity())));
                my_report_table_shelves.addCell(table_shelves_cell);
                table_shelves_cell=new PdfPCell(new Phrase(String.valueOf(shelfs.getRoom().getNumber())));
                my_report_table_shelves.addCell(table_shelves_cell);
            }
            my_pdf_report.add(my_report_table_shelves);
            my_pdf_report.close();
//            logger.info("Saved to PDF");
        }
        catch (FileNotFoundException | DocumentException | MyPDFException e)
        {
//            logger.warn("Exception " + e);
            e.printStackTrace();
        }
        refreshScreen(event);
    }


    public void ExitMainWindow() {

        logger.debug("Closing main window");

        exit_btn.setOnAction(SceneController::close);
    }

    public void WrapMainWindow() {

        logger.debug("Wrapping main window");

        wrap_btn.setOnAction(SceneController::wrap);
    }

    @FXML
    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getRoomsShelvesScene(event);
    }

    @FXML
    public void changeUser(ActionEvent event) throws IOException {
        SceneController.getLoginScene(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        warehouseService.getFullnessOfWarehouse();
        fullness_label.setText("Fullness: " + warehouseService.getFullnessOfWarehouse() + "%");

        table_shelves.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        choice_box.setValue("Rooms/Shelves");
        choice_box.getItems().addAll(choices);

        setShelfList();
        setRoomList();

        number_column.setCellValueFactory(new PropertyValueFactory<>("number"));
        capacity_column.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        room_column.setCellValueFactory(new PropertyValueFactory<>("room"));

        roomnumber_column.setCellValueFactory(new PropertyValueFactory<>("number"));
        roomcapacity_column.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        ObservableList<Room> roomsObservableList = FXCollections.observableArrayList(roomService.getRooms());

        table_shelves.setEditable(true);

        number_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        capacity_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        room_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(roomsObservableList));

        roomnumber_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        roomcapacity_column.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        table_shelves.setItems(getShelvesSortedList());
        table_rooms.setItems(getRoomsSortedList());
    }
}
