package com.umler.warehouses.Controllers;

import java.awt.*;
import java.io.*;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Model.*;
import com.umler.warehouses.Services.CompanyService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CompaniesController implements Initializable
{
    public Button exit_btn;

    public Button wrap_btn;
    @FXML
    public Hyperlink current_user;

    public Label search_invalid_label1;

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableColumn<Company, String> name_column;

    @FXML
    private TableColumn<Company, String> address_column;

    @FXML
    private TableColumn<Company, String> phone_column;

    @FXML
    private TableColumn<Company, String> tin_column;

    @FXML
    private TableView<Company> table;

    @FXML
    private TextField search;

    ObservableList<Company> CompanyList = FXCollections.observableArrayList();

    CompanyService companyService = new CompanyService();

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

    private final String[] choices = {"Companies","Contracts","Products","Shelves"};


    @FXML
    private void getChoices(ActionEvent event) throws IOException {

        logger.info("Choice box action");

        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Contracts"))
        {
            logger.info("Choice box Contracts selected");
            SceneController.getContractsScene(event);
        }
        if (Objects.equals(choice, "Products"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getProductsScene(event);
        }
        if (Objects.equals(choice, "Shelves"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getRoomsShelvesScene(event);
        }
    }

    public void setCompanyList() {
        CompanyList.clear();
        CompanyList.addAll(companyService.getCompanies());
    }

    private ObservableList<Company> getSortedList() {
        SortedList<Company> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    private FilteredList<Company> getFilteredList() {
        FilteredList<Company> filteredList = new FilteredList<>(CompanyList, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(company -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (company.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (company.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (company.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return company.getTin().toLowerCase().contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    @FXML
    private void delete(ActionEvent event)
    {
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

        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new myDeleteException();
        else {
            ObservableList<Company> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Company company : selectedRows) {
                companyService.deleteCompany(company);
            }
            refreshScreen(event);
        }
    }

    @FXML
    private void save()
    {
        try
        {
//            log.debug("saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_company.csv"));
            for(Company companies : CompanyList)
            {
                writer.write(companies.getName() + ";" + companies.getAddress() + ";"
                        + companies.getPhoneNumber() + ";"
                        + companies.getTin());
                writer.newLine();
            }
            writer.close();
            Desktop.getDesktop().open(new File("saves"));
//            log.info("saved to file");
        }
        catch (IOException e)
        {
//            log.warn("Exception " + e);
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
    public void editName(TableColumn.CellEditEvent<Company, String> editEvent)
    {
        Company selectedCompany = table.getSelectionModel().getSelectedItem();
        selectedCompany.setName(editEvent.getNewValue());
        companyService.updateCompany(selectedCompany);

        logger.debug("Editing cell");
    }

    @FXML
    public void editAddress(TableColumn.CellEditEvent<Company, String> editEvent)
    {
        Company selectedCompany = table.getSelectionModel().getSelectedItem();
        selectedCompany.setAddress(editEvent.getNewValue());
        companyService.updateCompany(selectedCompany);

        logger.debug("Editing cell");
    }

    @FXML
    public void editPhone(TableColumn.CellEditEvent<Company, String> editEvent)
    {
        Company selectedCompany = table.getSelectionModel().getSelectedItem();
        selectedCompany.setPhoneNumber(editEvent.getNewValue());
        companyService.updateCompany(selectedCompany);

        logger.debug("Editing cell");
    }

    @FXML
    public void editTIN(TableColumn.CellEditEvent<Company, String> editEvent)
    {
        Company selectedCompany = table.getSelectionModel().getSelectedItem();
        selectedCompany.setTin(editEvent.getNewValue());
        companyService.updateCompany(selectedCompany);

        logger.debug("Editing cell");
    }

    @FXML
    public void toPDF(ActionEvent event) throws IOException {
        try {
//            log.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(5);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Address", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Phone", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("TIN", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (CompanyList.isEmpty()) throw new MyPDFException();

            for(Company companies : CompanyList)
            {
                table_cell=new PdfPCell(new Phrase(companies.getName()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(companies.getAddress()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(companies.getPhoneNumber()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(companies.getTin()));
                my_report_table.addCell(table_cell);
            }
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();
//            log.info("Saved to PDF");
        }
        catch (FileNotFoundException | DocumentException | MyPDFException e)
        {
//            log.warn("Exception " + e);
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

    void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getCompaniesScene(event);
    }

    @FXML
    public void changeUser(ActionEvent event) throws IOException {
        SceneController.getLoginScene(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        current_user.setVisited(true);
        current_user.setText("User: " + CurrentUser.getCurrentUser().getName() + " / Change");
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        choice_box.setValue("Companies");
        choice_box.getItems().addAll(choices);

        setCompanyList();

        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("address"));
        phone_column.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tin_column.setCellValueFactory(new PropertyValueFactory<>("tin"));

        table.setEditable(true);

        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        address_column.setCellFactory(TextFieldTableCell.forTableColumn());
        phone_column.setCellFactory(TextFieldTableCell.forTableColumn());
        tin_column.setCellFactory(TextFieldTableCell.forTableColumn());

        table.setItems(getSortedList());
    }
}

