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

    private static final Logger logger = LoggerFactory.getLogger("Warehouse Logger");

    @FXML
    private void add(ActionEvent event) throws IOException {
        logger.debug("adding a room");

        NewWindowController.getNewContractCompanyWindow();
        if(UpdateStatus.isIsContractCompanyAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsContractCompanyAdded(false);
        }
        logger.info("room added");
    }


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

    private final String[] choices = {"Contracts","Companies", "Products", "Rooms/Shelves"};

    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        logger.info("Choice box action");

        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Companies"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getCompaniesScene(event);
        }
        if (Objects.equals(choice, "Products"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getProductsScene(event);
        }
        if (Objects.equals(choice, "Rooms/Shelves"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getRoomsShelvesScene(event);
        }
    }

    public void setContractList() {
        ContractList.clear();
        ContractList.addAll(contractService.getContracts());
    }

    private ObservableList<Contract> getSortedList() {
        SortedList<Contract> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    private FilteredList<Contract> getFilteredList() {
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

    private String date_converter(String temp){
        String[] temp2 = temp.split("-");
        return temp2[2] + '.' + temp2[1] + '.' + temp2[0];
    }

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

    private void deleteRows(ActionEvent event) throws myDeleteException, IOException {

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

    @FXML
    private void save()
    {
        try
        {
//            logger.debug("saving to file");
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_contract.csv"));
            for(Contract companies : ContractList)
            {
                writer.write(companies.getStartdate() + ";" + companies.getEnddate() + ";"
                        + companies.getNumber() + ";" + companies.getCompany().getName());
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
    public void editStartDate(TableColumn.CellEditEvent<Contract, LocalDate> editEvent) {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        System.out.println(editEvent.getNewValue());
        selectedContract.setStartdate(editEvent.getNewValue());
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }

    @FXML
    public void editEndDate(TableColumn.CellEditEvent<Contract, LocalDate> editEvent)
    {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        selectedContract.setEnddate(editEvent.getNewValue());
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }

    @FXML
    public void editNumber(TableColumn.CellEditEvent<Contract, Integer> editEvent)
    {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        selectedContract.setNumber(editEvent.getNewValue());
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }

    @FXML
    public void editCompany(TableColumn.CellEditEvent<Contract, Company> editEvent)
    {
        Contract selectedContract = table.getSelectionModel().getSelectedItem();
        Company company = editEvent.getNewValue();
        selectedContract.setCompany(company);
        contractService.updateContract(selectedContract);

        logger.debug("Editing cell");
    }


    @FXML
    public void toPDF(ActionEvent event) throws IOException {
        try {
//            logger.debug("Saving to PDF");
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
    public void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getContractsScene(event);
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

        current_user.setVisited(true);
        current_user.setText("User: " + CurrentUser.getCurrentUser().getName() + " / Change");
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        choice_box.setValue("Contracts");
        choice_box.getItems().addAll(choices);

        setContractList();

        startdate_column.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        enddate_column.setCellValueFactory(new PropertyValueFactory<>("enddate"));
        number_column.setCellValueFactory(new PropertyValueFactory<>("number"));
        company_column.setCellValueFactory(new PropertyValueFactory<>("company"));

        ObservableList<Company> companiesObservableList = FXCollections.observableArrayList(companyService.getCompanies());

        table.setEditable(true);

        startdate_column.setCellFactory(new LocalStartDateCellFactory());
        enddate_column.setCellFactory(new LocalEndDateCellFactory());
        number_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        company_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(companiesObservableList));

        table.setItems(getSortedList());
    }
}

