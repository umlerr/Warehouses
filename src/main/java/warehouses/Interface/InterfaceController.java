package warehouses.Interface;

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

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable
{
    @FXML
    private TableColumn<User, String> age_column;

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableColumn<User, String> id_column;

    @FXML
    private TableColumn<User, String> name_column;

    @FXML
    private TextField search;

    @FXML
    private TableColumn<User, String> surname_column;

    @FXML
    private TableView<User> table;

    @FXML
    private Label search_invalid_label;

    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    private void getChoices(ActionEvent event)
    {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Workers"))
        {
            System.out.println("Workers");
        }
    }

    ObservableList<User> List = FXCollections.observableArrayList();

    @FXML
    private void add(ActionEvent event)
    {
        List.add(new User("-","-","-","-"));
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
    private void save(ActionEvent event) throws IOException
    {
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save.csv"));
            for(User users : List)
            {
                writer.write(users.getID() + ";" + users.getName() + ";" + users.getSurname() + ";" + users.getAge());
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
    private void upload(ActionEvent event) throws IOException
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("saves/save.csv"));
            String temp;
            do{
                temp = reader.readLine();
                if(temp!=null){
                    String[] temp2 = temp.split(";");
                    List.add(new User(temp2[0],temp2[1],temp2[2],temp2[3]));
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
        FilteredList<User> filteredData = new FilteredList<>(List, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person ->
                    {
                        if (newValue == null || newValue.isEmpty()) return true;
                        String lowerCaseFilter = newValue.toLowerCase();
                        return person.getName().toLowerCase().contains(lowerCaseFilter) ||
                                person.getSurname().toLowerCase().contains(lowerCaseFilter) ||
                                person.getAge().toLowerCase().contains(lowerCaseFilter) ||
                                person.getID().toLowerCase().contains(lowerCaseFilter);
                    });
        });
        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public void onEditChanged1(TableColumn.CellEditEvent<User, String> userStringCellEditEvent)
    {
        User user = table.getSelectionModel().getSelectedItem();
        user.setID(userStringCellEditEvent.getNewValue());
    }
    public void onEditChanged2(TableColumn.CellEditEvent<User, String> userStringCellEditEvent)
    {
        User user = table.getSelectionModel().getSelectedItem();
        user.setName(userStringCellEditEvent.getNewValue());
    }
    public void onEditChanged3(TableColumn.CellEditEvent<User, String> userStringCellEditEvent)
    {
        User user = table.getSelectionModel().getSelectedItem();
        user.setSurname(userStringCellEditEvent.getNewValue());
    }
    public void onEditChanged4(TableColumn.CellEditEvent<User, String> userStringCellEditEvent)
    {
        User user = table.getSelectionModel().getSelectedItem();
        user.setAge(userStringCellEditEvent.getNewValue());
    }

    public void toPDF(ActionEvent actionEvent) throws Exception
    {
        try {
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(4);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            my_report_table.addCell(new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Surname", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Age", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));

            if (List.isEmpty()) throw new MyException();

            for(User users : List)
            {
                table_cell=new PdfPCell(new Phrase(users.getID()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(users.getName()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(users.getSurname()));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(users.getAge()));
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


    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        choice_box.getItems().addAll(choices);
        choice_box.setOnAction(this::getChoices);

        id_column.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));
        name_column.setCellValueFactory(new PropertyValueFactory<User, String>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<User, String>("Surname"));
        age_column.setCellValueFactory(new PropertyValueFactory<User, String>("Age"));


        table.setItems(List);
        table.setEditable(true);
        id_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        name_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        age_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());

        search();
    }





}

