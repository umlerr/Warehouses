package com.umler.warehouses.Controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.umler.warehouses.Converters.CustomIntegerStringConverter;
import com.umler.warehouses.Helpers.*;
import com.umler.warehouses.Model.Company;
import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Services.CompanyService;
import com.umler.warehouses.Services.ContractService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * Контроллер для таблицы контрактов.
 * @author Umler
 */
public class ContractsController implements Initializable
{

    @FXML
    public Button exit_btn;

    @FXML
    public Button wrap_btn;

    @FXML
    public Hyperlink current_user;

    @FXML
    public Label search_invalid_label1;

    @FXML
    public Button refresh_btn;

    @FXML
    public Label fullness_label;

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableColumn<Contract, LocalDate> startdate_column;

    @FXML
    private TableColumn<Contract, LocalDate> enddate_column;

    @FXML
    private TableColumn<Contract, Integer> number_column;

    @FXML
    private TableColumn<Contract, Company> company_column;

    @FXML
    private TableView<Contract> table;

    @FXML
    private TextField search;

    ObservableList<Contract> ContractList = FXCollections.observableArrayList();

    ContractService contractService = new ContractService();

    CompanyService companyService = new CompanyService();

    WarehouseService warehouseService = new WarehouseService();

    private static final Logger logger = LoggerFactory.getLogger("Contracts Logger");


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

    private final String[] choices = {"Contracts","Companies", "Products", "Rooms/Shelves"};

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
        if (Objects.equals(choice, "Products"))
        {
            logger.info("Choice box Products selected");
            SceneController.getProductsScene(event);
        }
        if (Objects.equals(choice, "Rooms/Shelves"))
        {
            logger.info("Choice box RoomsShelves selected");
            SceneController.getRoomsShelvesScene(event);
        }
    }

    /**
     * Обработчик события добавления нового контракта.
     * Вызывает метод NewWindowController для отображения окна добавления нового контракта.
     * Если контракт был успешно добавлен, обновляет экран с контрактами.
     * @param event Событие добавления нового контракта.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void add(ActionEvent event) throws IOException {
        logger.debug("adding a contract");

        NewWindowController.getNewContractCompanyWindow();
        if(UpdateStatus.isIsContractCompanyAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsContractCompanyAdded(false);
        }
        logger.info("contract added");
    }

    /**
     * Устанавливает список контрактов.
     * Добавляет список контрактов из БД.
     */
    private void setContractList() {
        logger.info("Contract list get from DB");
        ContractList.clear();
        ContractList.addAll(contractService.getContracts());
    }

    /**
     * Метод для получения отфильтрованного и отсортированного списка контрактов.
     * Создает новый отфильтрованный список на основе исходного списка контрактов, используя фильтр из searchField.
     * Затем создает новый отсортированный список на основе отфильтрованного списка и связывает его с компаратором таблицы.
     * @return Отсортированный список контрактов.
     */
    private ObservableList<Contract> getSortedList() {
        logger.info("ObservableList get");
        SortedList<Contract> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    /**
     * Метод для получения отфильтрованного списка контрактов на основе заданного текстового фильтра.
     * Создает новый отфильтрованный список на основе исходного списка контрактов, используя заданный текстовый фильтр.
     * Фильтр применяется к полям "Дата подписания", "Дата оканчания", "Номер контракта", "Компания, с которой заключен договор" каждого контракта.
     * @return Отфильтрованный список контрактов.
     */
    private FilteredList<Contract> getFilteredList() {
        logger.info("Filtering list with search");
        FilteredList<Contract> filteredList = new FilteredList<>(ContractList, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(contract -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (date_converter(contract.getStartdate().toString()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (date_converter(contract.getEnddate().toString()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(contract.getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return contract.getCompany().getName().toLowerCase().contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    /**
     * Конвертация даты для поиска в Российском формте dd.MM.yyyy.
     */
    private String date_converter(String temp){
        logger.info("Date converting for the search in Russian style");
        String[] temp2 = temp.split("-");
        return temp2[2] + '.' + temp2[1] + '.' + temp2[0];
    }

    /**
     * Обработчик события удаления выбранных контрактов из таблицы.
     * Удаляет выбранные контракты из таблицы.
     * Если ни один контракт не выбран, выбрасывает исключение myDeleteException.
     * После удаления контрактов обновляет экран.
     * @param event Событие удаления контрактов.
     */
    @FXML
    private void delete(ActionEvent event)
    {
        try {
            logger.debug("deleting a contract");
            deleteRows(event);
            logger.info("contract deleted");
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
     * Обработчик события удаления выбранных контрактов из таблицы.
     * Удаляет выбранные контракты из таблицы.
     * Если ни один контракт не выбран, выбрасывает исключение myDeleteException.
     * После удаления контрактов обновляет экран.
     * @param event Событие удаления контрактов.
     * @throws ContractsController.myDeleteException Если ни один контракт не выбрана.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    private void deleteRows(ActionEvent event) throws myDeleteException, IOException {
        logger.info("Deleting rows");
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new myDeleteException();
        else {
            ObservableList<Contract> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Contract contract : selectedRows) {
                contractService.deleteContract(contract);
            }
            refreshScreen(event);
        }
    }

    /**
     * Обработчик события сохранения списка контрактов в файл.
     * Сохраняет список контрактов в файл "saves/save_contract.csv".
     * Если произошла ошибка ввода-вывода при сохранении, выбрасывает исключение IOException.
     * После сохранения открывает папку "saves".
     */
    @FXML
    private void save()
    {
        try
        {
            logger.debug("saving to file");
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_contract.csv"));
            for(Contract companies : ContractList)
            {
                writer.write(companies.getStartdate() + ";" + companies.getEnddate() + ";"
                        + companies.getNumber() + ";" + companies.getCompany().getName());
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
     * Обработчик события изменения даты подписания контракта в таблице.
     * @param editEvent Событие изменения даты подписания компании в таблице.
     */
    @FXML
    private void editStartDate(TableColumn.CellEditEvent<Contract, LocalDate> editEvent) {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        System.out.println(editEvent.getNewValue());
        selectedContract.setStartdate(editEvent.getNewValue());
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события изменения даты окончания контракта в таблице.
     * @param editEvent Событие изменения даты окончания компании в таблице.
     */
    @FXML
    private void editEndDate(TableColumn.CellEditEvent<Contract, LocalDate> editEvent)
    {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        selectedContract.setEnddate(editEvent.getNewValue());
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события изменения номера контракта в таблице.
     * @param editEvent Событие изменения ИНН компании в таблице.
     */
    @FXML
    private void editNumber(TableColumn.CellEditEvent<Contract, Integer> editEvent)
    {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        selectedContract.setNumber(editEvent.getNewValue());
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }

    /**
     * Обработчик события изменения компании привязанной к контракту в таблице.
     * @param editEvent Событие изменения компании привязанной к контракту в таблице.
     */
    @FXML
    private void editCompany(TableColumn.CellEditEvent<Contract, Company> editEvent)
    {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        Company company = editEvent.getNewValue();
        selectedContract.setCompany(company);
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }


    /**
     * Обработчик события нажатия на кнопку сохранения таблицы контрактов в PDF файл.
     * Сохраняет данные из таблицы в файл "pdf/report_contracts.pdf".
     * Если список контрактов пуст, выбрасывает исключение MyPDFException.
     * Если возникает ошибка ввода-вывода, выводит сообщение об ошибке.
     */
    @FXML
    private void toPDF(ActionEvent event) throws IOException {
        try {
            logger.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report_contracts.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(4);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            my_report_table.addCell(new PdfPCell(new Phrase("StartDate", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("EndDate", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Number", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Company", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (ContractList.isEmpty()) throw new MyPDFException();

            for(Contract contracts : ContractList)
            {
                table_cell=new PdfPCell(new Phrase(String.valueOf(contracts.getStartdate())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(contracts.getEnddate())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(contracts.getNumber())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(contracts.getCompany().getName()));
                my_report_table.addCell(table_cell);
            }
            my_pdf_report.add(my_report_table);
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
     * Вызывает метод SceneController для отображения сцены с контрактами.
     * @param event Событие обновления экрана.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void refreshScreen(ActionEvent event) throws IOException {
        logger.info("Refreshing screen");
        SceneController.getContractsScene(event);
    }

    /**
     * Обработчик события смены экрана.
     * Вызывает метод SceneController для отображения сцены с авторизацией.
     * @param event Событие обновления экрана.
     * @throws IOException Если произошла ошибка ввода-вывода при загрузке сцены.
     */
    @FXML
    private void changeUser(ActionEvent event) throws IOException {
        logger.info("Changing user");
        SceneController.getLoginScene(event);
    }

    /**
     * Инициализация.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        logger.info("Contract initializing");

        logger.debug("Fullness getting");
        warehouseService.getFullnessOfWarehouse();
        fullness_label.setText("Fullness: " + warehouseService.getFullnessOfWarehouse() + "%");

        logger.debug("Current user getting");
        current_user.setVisited(true);
        current_user.setText("User: " + CurrentUser.getCurrentUser().getName() + " / Change");
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        logger.debug("Contract getting");
        choice_box.setValue("Contracts");
        choice_box.getItems().addAll(choices);

        setContractList();

        logger.debug("CellValueFactory setting");
        startdate_column.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        enddate_column.setCellValueFactory(new PropertyValueFactory<>("enddate"));
        number_column.setCellValueFactory(new PropertyValueFactory<>("number"));
        company_column.setCellValueFactory(new PropertyValueFactory<>("company"));

        logger.debug("Company list setting");
        ObservableList<Company> companiesObservableList = FXCollections.observableArrayList(companyService.getCompanies());

        logger.debug("Editable table setting");
        table.setEditable(true);

        logger.debug("CellFactory setting");
        startdate_column.setCellFactory(new LocalStartDateCellFactory());
        enddate_column.setCellFactory(new LocalEndDateCellFactory());
        number_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        company_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(companiesObservableList));

        logger.info("Table setting items");
        table.setItems(getSortedList());
    }
}

