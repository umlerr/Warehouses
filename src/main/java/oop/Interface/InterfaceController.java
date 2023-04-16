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


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable {


    @FXML
    private TableColumn<User, Integer> age_column;

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableColumn<User, Integer> id_column;

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
            new User("David","Airapetov",21,1),
            new User("Andrey","Vinogradov",20,2),
            new User("Klim","Nikolaev",19,3),
            new User("Vlad","Talankov",19,4)
    );
    @FXML
    private void add(ActionEvent event)
    {
        List.add(new User("Misha","Ugryumov",19,5));
        table.setItems(List);
    }
    @FXML
    private void delete(ActionEvent event)
    {
        int selectedID = table.getSelectionModel().getSelectedIndex();
        table.getItems().remove(selectedID);
    }
    @FXML
    private void edit(ActionEvent event)
    {
        int selectedID = table.getSelectionModel().getSelectedIndex();
        table.getItems().remove(selectedID);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choice_box.getItems().addAll(choices);
        choice_box.setOnAction(this::getChoices);

        name_column.setCellValueFactory(new PropertyValueFactory<User, String>("Name"));
        surname_column.setCellValueFactory(new PropertyValueFactory<User, String>("Surname"));
        age_column.setCellValueFactory(new PropertyValueFactory<User, Integer>("Age"));
        id_column.setCellValueFactory(new PropertyValueFactory<User, Integer>("ID"));

        name_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        surname_column.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        age_column.setCellFactory();
        id_column.setCellFactory();

        table.setItems(List);
    }


}