package com.umler.warehouses.AddControllers;

import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.DistinctObservableList;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Helpers.ComboBoxUtil;
import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Model.Product;
import com.umler.warehouses.Model.Shelf;
import com.umler.warehouses.Services.ContractService;
import com.umler.warehouses.Services.ProductService;
import com.umler.warehouses.Services.ShelfService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;


import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddProductController implements Initializable {

    @FXML
    public ChoiceBox<Contract> contract_choicebox;
    @FXML
    public ChoiceBox<Shelf> shelf_choicebox;
    @FXML
    public TextField quantity_field;
    @FXML
    public TextField type_field;
    @FXML
    public TextField name_field;
    @FXML
    public ComboBox<Product> type_comboBox;
    @FXML
    public ComboBox<Product> name_comboBox;

    ContractService contractService = new ContractService();
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
    private void getChoicesName() {
        Product product = name_comboBox.getValue();
        if (!Objects.equals(product, null))
        {
            name_field.setEditable(false);
            name_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            name_field.setText(product.getName());
        }
        else
        {
            name_field.setEditable(true);
            name_field.setStyle(null);
        }
    }

    @FXML
    private void getChoicesType() {
        Product product = type_comboBox.getValue();
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
                || shelf_choicebox.getValue() == null) {
            IOAlert.setContentText("You must fill out the product to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (contract_choicebox.getValue() == null){
            IOAlert.setContentText("You must add contract first to add products");
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
        if(!isShelfFree(shelf_choicebox.getValue().getNumber(), Integer.parseInt(quantity_field.getText())))
        {
            IOAlert.setContentText("Not enough space on the shelf to add new product!");
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
        product.setContract(contract_choicebox.getValue());
        return product;
    }

    private static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    private boolean isShelfFree(Integer number, Integer quantity){
        Shelf shelf = shelfService.getShelf(number);
        List<Product> products = shelf.getProductList();
        Integer fullness = 0;
        for (Product product : products) {
            fullness += product.getQuantity();
        }
        return fullness + quantity <= shelf.getCapacity();
    }
    private static boolean isNumeric(String str) {
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
        ObservableList<Contract> contractObservableList = FXCollections.observableArrayList(contractService.getContracts());
        ObservableList<Shelf> shelfObservableList = FXCollections.observableArrayList(shelfService.getShelves());

        ObservableList<Product> productObservableList = FXCollections.observableArrayList(productService.getProducts());

        DistinctObservableList<Product> distinctListNames = new DistinctObservableList<>(productObservableList);
        distinctListNames.distinct(Product::getName);
        DistinctObservableList<Product> distinctListTypes = new DistinctObservableList<>(productObservableList);
        distinctListTypes.distinct(Product::getType);

        name_comboBox.getItems().addAll(distinctListNames.getFilteredList());
        type_comboBox.getItems().addAll(distinctListTypes.getFilteredList());

        ComboBoxUtil.configureNameComboBox(name_comboBox);
        ComboBoxUtil.configureTypeComboBox(type_comboBox);

        contract_choicebox.getItems().addAll(contractObservableList);
        shelf_choicebox.getItems().addAll(shelfObservableList);
    }
}
