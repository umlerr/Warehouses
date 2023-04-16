package oop.Interface;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;


import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InterfaceController implements Initializable {

    @FXML
    private ChoiceBox<String> choice_box;

    @FXML
    private TableView<?> table;

    private String[] choices = {"Workers","Clients","Rooms","Reports"};

    private void getChoices(ActionEvent event)
    {
        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Workers"))
        {
            System.out.println("IDI NAHUY");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        choice_box.getItems().addAll(choices);
        choice_box.setOnAction(this::getChoices);
    }


}