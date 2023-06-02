package com.umler.warehouses.AddControllers;

import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Company;
import com.umler.warehouses.Model.Product;
import com.umler.warehouses.Model.Shelf;
import com.umler.warehouses.Services.CompanyService;
import com.umler.warehouses.Services.ProductService;
import com.umler.warehouses.Services.ShelfService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddShelfController implements Initializable {

    @FXML
    public ChoiceBox<Company> company_choicebox;
    @FXML
    public ChoiceBox<Shelf> shelf_choicebox;
    @FXML
    public TextField quantity_field;
    @FXML
    public TextField type_field;
    @FXML
    public TextField name_field;
    @FXML
    public ChoiceBox<Product> product_choicebox;

    CompanyService companyService = new CompanyService();
    ProductService productService = new ProductService();
    ShelfService shelfService = new ShelfService();

    @FXML
    private void saveNewProductToDb(ActionEvent event){
        if (validateInputs()) {
            Product product = createProductFromInput();

            boolean isSaved = new ProductService().createProduct(product);
            if (isSaved) {
                UpdateStatus.setIsProductAdded(true);
                delayWindowClose(event);
            }
        }
    }

    @FXML
    private void getChoices() {
        Product product = product_choicebox.getValue();
        if (!Objects.equals(product, null))
        {
            type_field.setEditable(false);
            type_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            type_field.setText(product.getType());
        }
        else
        {
            type_field.setEditable(true);
            type_field.setStyle(null);
        }
    }

    private boolean validateInputs() {
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (name_field.getText().equals("")
                || type_field.getText().equals("")
                || quantity_field.getText().equals("")
                || shelf_choicebox.getValue() == null
                || company_choicebox.getValue() == null) {
            IOAlert.setContentText("You must fill out the product to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (isNumeric(quantity_field.getText())){
            IOAlert.setContentText("Incorrect input for product number - you must put a positive number");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        return true;
    }

    private Product createProductFromInput() {
        Product product = new Product();
        product.setName(capitalize(name_field.getText().toLowerCase()));
        product.setType(capitalize(type_field.getText().toLowerCase()));
        product.setQuantity(Integer.valueOf(quantity_field.getText()));
        product.setShelf(shelf_choicebox.getValue());
        product.setCompany(company_choicebox.getValue());
        return product;
    }

    public static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public static boolean isNumeric(String str) {
        try {
            return Double.parseDouble(str) <= 0;
        } catch(NumberFormatException e){
            return true;
        }
    }

    private void delayWindowClose(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event2 -> closeWindow(event));
        delay.play();
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        SceneController.close(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        ObservableList<Company> companyObservableList = FXCollections.observableArrayList(companyService.getCompanies());
        ObservableList<Shelf> shelfObservableList = FXCollections.observableArrayList(shelfService.getShelfs());
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productService.getProducts());
        product_choicebox.getItems().addAll(productObservableList);
        company_choicebox.getItems().addAll(companyObservableList);
        shelf_choicebox.getItems().addAll(shelfObservableList);
    }
}
