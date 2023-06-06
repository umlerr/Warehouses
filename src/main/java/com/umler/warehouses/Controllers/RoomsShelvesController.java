package com.umler.warehouses.Controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Product;
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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Контроллер для таблицы помещений и стеллажей.
 * @author Umler
 */
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

    /**
     * Мое исключение для записи в PDF файл
     */
    static class MyPDFException extends Exception
    {
        public MyPDFException()
        {
            super("There is nothing to save");
        }
    }

    /**
     * Мое исключение для записи в удаления строк таблицы
     */
    static class myDeleteException extends Exception
    {
        public myDeleteException()
        {
            super("Choose a row to delete");
        }
    }

    private final String[] choices = {"Rooms/Shelves","Products","Companies", "Contracts"};

    /**
     * Обработчик события выбора значения в выпадающем списке.
     * Получает выбранное значение и вызывает соответствующий метод в классе SceneController для отображения соответствующей сцены.
     * @param event событие выбора значения в выпадающем списке
     * @throws IOException если возникает ошибка ввода-вывода при отображении сцены
     */
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

    /**
     * Устанавливает список помещений.
     * Добавляет список помещений из БД.
     */
    private void setRoomList() {
        RoomList.clear();
        RoomList.addAll(roomService.getRooms());
    }

    /**
     * Метод для получения отфильтрованного и отсортированного списка помещений.
     * Создает новый отфильтрованный список на основе исходного списка помещений, используя фильтр из searchField.
     * Затем создает новый отсортированный список на основе отфильтрованного списка и связывает его с компаратором таблицы.
     * @return Отсортированный список помещений.
     */
    private ObservableList<Room> getRoomsSortedList() {
        SortedList<Room> sortedList = new SortedList<>(getRoomsFilteredList());
        sortedList.comparatorProperty().bind(table_rooms.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка помещений на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка помещений, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "Номер", "Вместимость" каждого помещения.
     * @return Отфильтрованный список помещений.
     */
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

    /**
     * Обработчик события добавления нового помещения.
     * Вызывает метод NewWindowController для отображения окна добавления нового помещения.
     * Если помещение был успешно добавлено, обновляет экран с помещениями.
     * @param event Событие добавления нового помещения.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
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

    /**
     * Обработчик события удаления выбранных помещений из таблицы.
     * Удаляет выбранные помещения из таблицы.
     * Если ни одно помещение не выбрано, выбрасывает исключение myDeleteException.
     * После удаления помещений обновляет экран.
     * @param event Событие удаления помещений.
     */
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

    /**
     * Обработчик события удаления выбранных помещений из таблицы.
     * Удаляет выбранные помещения из таблицы.
     * Если ни одно помещение не выбрано, выбрасывает исключение myDeleteException.
     * @param event После удаления помещений обновляет экран.
     * @throws RoomsShelvesController.myDeleteException Если ни одно помещение не выбрано.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
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

    /**
     * Обработчик события сохранения списка помещений в файл.
     * Сохраняет список помещений в файл "saves/save_room.csv".
     * Если произошла ошибка ввода-вывода при сохранении, выбрасывает исключение IOException.
     * После сохранения открывает папку "saves".
     */
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

    /**
     * Обработчик события изменения номера помещения в таблице.
     * Проверка на существования помещения с введеным для изменения номером.
     * Вывод ошибки если помещение с таким номером уже существует.
     * @param editEvent Событие изменения названия помещения в таблице.
     */
    @FXML
    private void editRoomNumber(TableColumn.CellEditEvent<Room, Integer> editEvent)
    {
        Room selectedRoom = table_rooms.getSelectionModel().getSelectedItem();

        for (Room rooms : roomService.getRooms()){
            if (!Objects.equals(editEvent.getNewValue(), rooms.getNumber()))
            {
                selectedRoom.setNumber(editEvent.getNewValue());
                roomService.updateRoom(selectedRoom);
            }
            else
            {
                logger.warn("Room with this number already exist");
                Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Room existence", ButtonType.OK);
                IOAlert.setContentText("Room with this number already exist / Click the refresh button and try again");
                IOAlert.showAndWait();
                if(IOAlert.getResult() == ButtonType.OK)
                {
                    IOAlert.close();
                }
                break;
            }
        }
    }

    /**
     * Обработчик события изменения вместительности помещения в таблице.
     * @param editEvent Событие изменения вместительности помещения в таблице.
     */
    @FXML
    private void editRoomCapacity(TableColumn.CellEditEvent<Room, Integer> editEvent)
    {
        Room selectedRoom = table_rooms.getSelectionModel().getSelectedItem();
        selectedRoom.setCapacity(editEvent.getNewValue());
        roomService.updateRoom(selectedRoom);

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события нажатия на кнопку сохранения таблицы помещений в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_rooms.pdf".
     * Если список помещений пуст, выбрасывает исключение MyPDFException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    @FXML
    private void toPDFRooms(ActionEvent event) throws IOException {
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
            logger.warn("Exception " + e);
            e.printStackTrace();
        }
        refreshScreen(event);
    }

    /**
     * Устанавливает список стеллажей.
     * Добавляет список товаров из БД.
     */
    private void setShelfList() {
        ShelfList.clear();
        ShelfList.addAll(shelfService.getShelves());
    }

    /**
     * Метод для получения отфильтрованного и отсортированного списка стеллажей.
     * Создает новый отфильтрованный список на основе исходного списка стеллажей, используя фильтр из searchField.
     * Затем создает новый отсортированный список на основе отфильтрованного списка и связывает его с компаратором таблицы.
     * @return Отсортированный список стеллажей.
     */
    private ObservableList<Shelf> getShelvesSortedList() {
        SortedList<Shelf> sortedList = new SortedList<>(getShelvesFilteredList());
        sortedList.comparatorProperty().bind(table_shelves.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка стеллажей на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка стеллажей, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "Номер", "Вместительность", "Помещение, в котором стоит стеллаж" каждого стеллажа.
     * @return Отфильтрованный список стеллажей.
     */
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

    /**
     * Обработчик события добавления нового стеллажа.
     * Вызывает метод NewWindowController для отображения окна добавления нового стеллажа.
     * Если стеллаж был успешно добавлен, обновляет экран с стеллажами.
     * @param event Событие добавления нового стеллажа.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
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

    /**
     * Обработчик события удаления выбранных стеллажей из таблицы.
     * Удаляет выбранные стеллажи из таблицы.
     * Если ни один стеллаж не выбран, выбрасывает исключение myDeleteException.
     * После удаления стеллажей обновляет экран.
     * @param event Событие удаления стеллажей.
     */
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

    /**
     * Обработчик события удаления выбранных стеллажей из таблицы.
     * Удаляет выбранные стеллажи из таблицы.
     * Если ни один стеллаж не выбран, выбрасывает исключение myDeleteException.
     * После удаления стеллажей обновляет экран.
     * @param event Событие удаления стеллажей.
     * @throws RoomsShelvesController.myDeleteException Если ни один стеллаж не выбран.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
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
    /**
     * Обработчик события сохранения списка стеллажей в файл.
     * Сохраняет список стеллажей в файл "saves/save_shelf.csv".
     * Если произошла ошибка ввода-вывода при сохранении, выбрасывает исключение IOException.
     * После сохранения открывает папку "saves".
     */
    @FXML
    private void saveShelves()
    {
        try
        {
            logger.debug("saving to file");
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_shelf.csv"));
            for(Shelf shelves : ShelfList)
            {
                writer.write(shelves.getNumber() + ";" + shelves.getCapacity() + ";"
                        + shelves.getRoom().getNumber());
                writer.newLine();
            }
            writer.close();
            Desktop.getDesktop().open(new File("saves"));
            logger.info("saved to file");
        }
        catch (IOException e)
        {
            logger.warn("Exception " + e);
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error!", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    /**
     * Обработчик события изменения номера стеллажа в таблице.
     * Проверка на существования стеллажа с введеным для изменения номером.
     * Вывод ошибки если стеллаж с таким номером уже существует.
     * @param editEvent Событие изменения названия стеллажа в таблице.
     */
    @FXML
    private void editShelfNumber(TableColumn.CellEditEvent<Shelf, Integer> editEvent)
    {
        Shelf selectedShelf = table_shelves.getSelectionModel().getSelectedItem();

        for (Shelf shelves : shelfService.getShelves()){
            if (!Objects.equals(editEvent.getNewValue(), shelves.getNumber()))
            {
                selectedShelf.setNumber(editEvent.getNewValue());
                shelfService.updateShelf(selectedShelf);
            }
            else
            {
                logger.warn("Shelf with this number already exist");
                Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Shelf existence", ButtonType.OK);
                IOAlert.setContentText("Shelf with this number already exist / Click the refresh button and try again");
                IOAlert.showAndWait();
                if(IOAlert.getResult() == ButtonType.OK)
                {
                    IOAlert.close();
                }
                break;
            }
        }

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события изменения вместительности стеллажа в таблице.
     * Проверка на кол-во товара уже на стеллаже.
     * Вывод ошибки если стеллаж заполнен больше чем пользователь пытается ввести.
     * @param editEvent Событие изменения вместительности стеллажа в таблице.
     */
    @FXML
    private void editShelfCapacity(TableColumn.CellEditEvent<Shelf, Integer> editEvent)
    {
        Shelf selectedShelf = table_shelves.getSelectionModel().getSelectedItem();
        Integer product_space = 0;
        List<Product> products = selectedShelf.getProductList();

        for(Product product : products)
        {
            product_space += product.getQuantity();
        }
        if (editEvent.getNewValue() >= product_space)
        {
            selectedShelf.setCapacity(editEvent.getNewValue());
            shelfService.updateShelf(selectedShelf);
        }
        else
        {
            logger.error("MyAddException");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "MyAddException", ButtonType.OK);
            IOAlert.setContentText("You can't put this capacity products occupy much more space / Retry!");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            table_shelves.refresh();
        }

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события изменения помещения в котором находится стеллаж.
     * @param editEvent Событие изменения помещения в котором находится стеллаж.
     */
    @FXML
    private void editRoom(TableColumn.CellEditEvent<Shelf, Room> editEvent)
    {
        Shelf selectedShelf = table_shelves.getSelectionModel().getSelectedItem();
        Room room = editEvent.getNewValue();
        selectedShelf.setRoom(room);
        shelfService.updateShelf(selectedShelf);

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события нажатия на кнопку сохранения таблицы товаров в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_shelves.pdf".
     * Если список товаров пуст, выбрасывает исключение MyPDFException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    @FXML
    private void toPDFShelves(ActionEvent event) throws IOException {
        try {
//            logger.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report_shelves.pdf"));
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
            logger.info("Saved to PDF");
        }
        catch (FileNotFoundException | DocumentException | MyPDFException e)
        {
            logger.warn("Exception " + e);
            e.printStackTrace();
        }
        refreshScreen(event);
    }

    /**
     * Выход из окна программы.
     */
    public void ExitMainWindow() {

        logger.debug("Closing main window");

        exit_btn.setOnAction(SceneController::close);
    }

    /**
     * Сворачивание окна программы.
     */
    public void WrapMainWindow() {

        logger.debug("Wrapping main window");

        wrap_btn.setOnAction(SceneController::wrap);
    }

    /**
     * Обработчик события обновления экрана.
     * Вызывает метод SceneController для отображения сцены с помещениями и стеллажами.
     * @param event Событие обновления экрана.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getRoomsShelvesScene(event);
    }

    /**
     * Инициализация.
     */
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

