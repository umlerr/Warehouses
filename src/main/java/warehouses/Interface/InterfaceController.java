package warehouses.Interface;

import java.io.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable
{
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

    private final String[] choices = {"Managers","Contracts","Rooms"};

    private void getChoices(ActionEvent event)
    {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Managers"))
        {
            System.out.println("Managers");
        }
    }

    ObservableList<Manager> List = FXCollections.observableArrayList();

    @FXML
    private void add(ActionEvent event)
    {
        List.add(new Manager("-","-","-"));
        table.setItems(List);
    }

    private void remove_row() throws MyException
    {
        table.setItems(List);
        int selectedID = table.getSelectionModel().getSelectedIndex();
        if (selectedID == -1) throw new MyException();
        else table.getItems().remove(selectedID);
    }

    static class MyException extends Exception
    {
        public MyException()
        {
            super("Choose a row to delete");
        }
    }

    @FXML
    private void delete(ActionEvent event)
    {
        try {
            search_invalid_label.setText("");
            remove_row();
        }
        catch (MyException myEx){
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
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save.csv"));
            for(Manager users : List)
            {
                writer.write(users.getID() + ";" + users.getName() + ";" + users.getSurname());
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e)
        {
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
            BufferedReader reader = new BufferedReader(new FileReader("saves/save.csv"));
            String temp;
            do{
                temp = reader.readLine();
                if(temp!=null){
                    String[] temp2 = temp.split(";");
                    List.add(new Manager(temp2[0],temp2[1],temp2[2]));
                }
            }
            while(temp!=null);
            table.setItems(List);
            reader.close();
        }
        catch (IOException e)
        {
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
            IOAlert.setContentText("Error");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    @FXML
    private void search()
    {
        FilteredList<Manager> filteredData = new FilteredList<>(List, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    {
                        if (newValue == null || newValue.isEmpty()) return true;
                        String lowerCaseFilter = newValue.toLowerCase();
                        return person.getName().toLowerCase().contains(lowerCaseFilter) ||
                                person.getSurname().toLowerCase().contains(lowerCaseFilter) ||
                                person.getID().toLowerCase().contains(lowerCaseFilter);
                    });
        });
        SortedList<Manager> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public void onEditChanged1(TableColumn.CellEditEvent<Manager, String> userStringCellEditEvent)
    {
        Manager manager = table.getSelectionModel().getSelectedItem();
        manager.setID(userStringCellEditEvent.getNewValue());
    }
    public void onEditChanged2(TableColumn.CellEditEvent<Manager, String> userStringCellEditEvent)
    {
        Manager manager = table.getSelectionModel().getSelectedItem();
        manager.setName(userStringCellEditEvent.getNewValue());
    }
    public void onEditChanged3(TableColumn.CellEditEvent<Manager, String> userStringCellEditEvent)
    {
        Manager manager = table.getSelectionModel().getSelectedItem();
        manager.setSurname(userStringCellEditEvent.getNewValue());
    }

    public void toPDF(ActionEvent actionEvent)
    {
        try {
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
                table_cell=new PdfPCell(new Phrase(managers.getID()));
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
            e.printStackTrace();
        }
    }

    private void addHeader(PdfWriter writer){
        PdfPTable header = new PdfPTable(2);
        try {
            // set defaults
            header.setWidths(new int[]{2, 24});
            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(40);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add text
            PdfPCell text = new PdfPCell();
            text.setPaddingBottom(15);
            text.setPaddingLeft(10);
            text.setBorder(Rectangle.BOTTOM);
            text.setBorderColor(BaseColor.LIGHT_GRAY);
            text.addElement(new Phrase("iText PDF Header Footer Example", new Font(Font.FontFamily.HELVETICA, 12)));
            text.addElement(new Phrase("https://memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 8)));
            header.addCell(text);

            // write content
            header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
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

        search();
    }





}

