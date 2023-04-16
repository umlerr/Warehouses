package oop.Interface;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;


import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable {


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
    private Button plus_btn;


    private final String[] choices = {"Workers","Clients","Rooms","Reports"};

    private void getChoices(ActionEvent event)
    {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Workers"))
        {
            System.out.println("IDI NAHUY");
        }
    }

    ObservableList<User> List = FXCollections.observableArrayList(
            new User("1","David","Airapetov","20"),

            new User("2","Andrey","Vinogradov","20"),
            new User("3","Klim","Nikolaev","19"),
            new User("4","Vlad","Talankov","19")
    );
    @FXML
    private void add(ActionEvent event)
    {
        List.add(new User("Misha","Ugryumov","19","5"));
        table.setItems(List);
    }
    @FXML
    private void delete(ActionEvent event)
    {
        int selectedID = table.getSelectionModel().getSelectedIndex();
        table.getItems().remove(selectedID);
    }
    @FXML
    private void save(ActionEvent event) throws IOException {
        try {
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
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "POSHEL NAHUY!", ButtonType.OK);
            IOAlert.setContentText("Sorry, David, you are gay");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }
    @FXML
    private void upload(ActionEvent event) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("saves/save.csv"));
            ObservableList<User> List = FXCollections.observableArrayList();
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
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "POSHEL NAHUY!", ButtonType.OK);
            IOAlert.setContentText("Sorry, David, you are gay");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choice_box.getItems().addAll(choices);
        choice_box.setOnAction(this::getChoices);

        name_column.setCellValueFactory(new PropertyValueFactory<User, String>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<User, String>("Surname"));
        age_column.setCellValueFactory(new PropertyValueFactory<User, String>("Age"));
        id_column.setCellValueFactory(new PropertyValueFactory<User, String>("ID"));

        name_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        age_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        id_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());

        table.setItems(List);
    }


}