package com.umler.warehouses.Controllers;

import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Model.Manager;
import com.umler.warehouses.Model.Warehouse;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.transformation.FilteredList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable
{
    public Button exit_btn;

    public Button wrap_btn;

    public Button back_btn1;
    @FXML
    public Hyperlink current_user;

    private Warehouse SelectedWarehouse;

    public Label main_label;

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableColumn<Manager, String> id_column;

    @FXML
    private TableColumn<Manager, String> name_column;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<Manager, String> surname_column;

    @FXML
    private TableView<Manager> table;

    @FXML
    private Label search_invalid_label;

    ObservableList<Manager> List = FXCollections.observableArrayList();

    private static final Logger logger = LoggerFactory.getLogger("Warehouse Logger");


    public void toWarehouse(Warehouse warehouse)
    {
        logger.info("Warehouse selected");

        SelectedWarehouse = warehouse;
        main_label.setText("Warehouse #" + SelectedWarehouse.getId_warehouse());
    }

    public void BackToWarehouses(ActionEvent event) throws IOException {
        SceneController.getWarehousesScene(event);
    }

    public void toLogin(ActionEvent event) throws IOException {
        SceneController.getLoginScene(event);
    }

    static class MyException extends Exception
    {
        public MyException()
        {
            super("Choose a row to delete");
        }
    }

    private final String[] choices = {"Managers","Contracts","Rooms"};

    private void getChoices(ActionEvent event)
    {

        logger.info("Choice box action");

        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Managers"))
        {
            logger.info("Choice box Managers selected");
            System.out.println("Managers");
        }
    }

    @FXML
    private void add(ActionEvent event)
    {
        logger.info("Adding manager");

        List.add(new Manager());
        table.setItems(List);

        logger.info("Manager added");
    }

    private void remove_row() throws MyException
    {
        table.setItems(List);
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else table.getItems().remove(selectedID);
    }

    @FXML
    private void delete(ActionEvent event)
    {
        try {

            logger.info("Manager added");

            search_invalid_label.setText("");
            remove_row();
        }
        catch (MyException myEx)
        {

            logger.warn("MyException " + myEx);

            search_invalid_label.setText("Choose a row to delete");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, myEx.getMessage(), ButtonType.OK);
            IOAlert.setContentText(myEx.getMessage());
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    @FXML
    private void save(ActionEvent event)
    {
        try
        {

            logger.info("Saving to file");

            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save.csv"));
            for(Manager users : List)
            {
                writer.write(users.getId_manager() + ";" + users.getName() + ";" + users.getSurname());
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e)
        {

            logger.warn("IOException " + e);

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
    private void upload(ActionEvent event)
    {
        try
        {

            logger.debug("Uploading from file");

            BufferedReader reader = new BufferedReader(new FileReader("saves/save.csv"));
            String temp;
            List.clear();
            do{
                temp = reader.readLine();
                if(temp!=null){
                    String[] temp2 = temp.split(";");
                    List.add(new Manager());
                }
            }
            while(temp!=null);
            table.setItems(List);
            reader.close();
        }
        catch (IOException e)
        {

            logger.warn("IOException" + e);

            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }


    public void onEditChanged1(TableColumn.CellEditEvent<Manager, Integer> userStringCellEditEvent)
    {
        Manager manager = table.getSelectionModel().getSelectedItem();
        manager.setId_manager(userStringCellEditEvent.getNewValue());

        logger.debug("Editing cell");

    }
    public void onEditChanged2(TableColumn.CellEditEvent<Manager, String> userStringCellEditEvent)
    {
        Manager manager = table.getSelectionModel().getSelectedItem();
        manager.setName(userStringCellEditEvent.getNewValue());

        logger.debug("Editing cell");
    }
    public void onEditChanged3(TableColumn.CellEditEvent<Manager, String> userStringCellEditEvent)
    {
        Manager manager = table.getSelectionModel().getSelectedItem();
        manager.setSurname(userStringCellEditEvent.getNewValue());

        logger.debug("Editing cell");
    }

    public void toPDF(ActionEvent actionEvent)
    {
        try {

            logger.debug("Saving to PDF");

            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report.pdf"));
            my_pdf_report.open();

            Phrase phrase = new Phrase("REPORT", FontFactory.getFont(FontFactory.COURIER_BOLD, 56,Font.BOLD));


            PdfPTable my_report_table = new PdfPTable(3);

            my_pdf_report.add(phrase);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            my_report_table.addCell(new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Surname", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));

            if (List.isEmpty()) throw new MyException();

            for(Manager managers : List)
            {
                table_cell=new PdfPCell(new Phrase(managers.getId_manager()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(managers.getName()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(managers.getSurname()));
                my_report_table.addCell(table_cell);
            }
            my_pdf_report.add(my_report_table);
            my_pdf_report.close();
        }
        catch (FileNotFoundException | DocumentException | MyException e)
        {

            logger.warn("File not found" + e);
            e.printStackTrace();
        }
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
    public void changeUser(ActionEvent event) throws IOException {
        SceneController.getLoginScene(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        current_user.setText(CurrentUser.getCurrentUser().getName() + " / Change");
        choice_box.getItems().addAll(choices);
        choice_box.setOnAction(this::getChoices);

        id_column.setCellValueFactory(new PropertyValueFactory<>("ID"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<>("Surname"));


        table.setItems(List);
        table.setEditable(true);
        id_column.setCellFactory(TextFieldTableCell.forTableColumn());
        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.forTableColumn());
    }





}

