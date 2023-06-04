package com.umler.warehouses.Controllers;

import com.itextpdf.text.Font;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.umler.warehouses.Converters.CustomIntegerStringConverter;
import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Model.Product;
import com.umler.warehouses.Model.Shelf;
import com.umler.warehouses.Services.ContractService;
import com.umler.warehouses.Services.ProductService;
import com.umler.warehouses.Services.ShelfService;
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
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductsController implements Initializable
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
    public TableColumn<Product, String> name_column;
    @FXML
    public TableColumn<Product, String> type_column;
    @FXML
    public TableColumn<Product, Integer> quantity_column;
    @FXML
    public TableColumn<Product, Shelf> shelf_column;
    @FXML
    public Button refresh_btn;
    @FXML
    private TableColumn<Product, Contract> contract_column;
    @FXML
    private ChoiceBox<String> choice_box;
    @FXML
    private TableView<Product> table;
    @FXML
    private TextField search;

    @FXML
    public Label fullness_label;

    WarehouseService warehouseService = new WarehouseService();

    ObservableList<Product> ProductList = FXCollections.observableArrayList();

    ProductService productService = new ProductService();

    ContractService contractService = new ContractService();

    ShelfService shelfService = new ShelfService();

    private static final Logger logger = LoggerFactory.getLogger("Warehouse Logger");

    @FXML
    private void add(ActionEvent event) throws IOException {
        logger.debug("adding a room");
        NewWindowController.getNewProductWindow();
        if(UpdateStatus.isIsProductAdded()) {
            refreshScreen(event);
            UpdateStatus.setIsProductAdded(false);
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

    private final String[] choices = {"Products","Companies", "Contracts", "Rooms/Shelves"};

    @FXML
    private void getChoices(ActionEvent event) throws IOException {
        logger.info("Choice box action");

        String choice = choice_box.getValue();
        if (Objects.equals(choice, "Companies"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getCompaniesScene(event);
        }
        if (Objects.equals(choice, "Contracts"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getContractsScene(event);
        }
        if (Objects.equals(choice, "Rooms/Shelves"))
        {
            logger.info("Choice box Managers selected");
            SceneController.getRoomsShelvesScene(event);
        }
    }

    private void setProductList() {
        ProductList.clear();
        ProductList.addAll(productService.getProducts());
    }

    private ObservableList<Product> getSortedList() {
        SortedList<Product> sortedList = new SortedList<>(getFilteredList());
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        return sortedList;
    }

    private FilteredList<Product> getFilteredList() {
        FilteredList<Product> filteredList = new FilteredList<>(ProductList, b -> true);
        search.textProperty().addListener((observable, oldValue, newValue) ->
                filteredList.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (product.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (product.getType().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(product.getQuantity()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else return String.valueOf(product.getContract().getNumber()).toLowerCase().contains(lowerCaseFilter);
                }));
        return filteredList;
    }

    @FXML
    private void delete(ActionEvent event)
    {
        try {

            logger.debug("deleting a product");
            deleteRows(event);
            logger.info("product deleted");
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
            ObservableList<Product> selectedRows = table.getSelectionModel().getSelectedItems();
            for (Product product : selectedRows) {
                productService.deleteProduct(product);
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
            BufferedWriter writer = new BufferedWriter(new FileWriter("saves/save_product.csv"));
            for(Product companies : ProductList)
            {
                writer.write(companies.getName() + ";" + companies.getType() + ";"
                        + companies.getQuantity() + ";" + companies.getContract().getNumber());
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
    private void editName(TableColumn.CellEditEvent<Product, String> editEvent)
    {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        selectedProduct.setName(editEvent.getNewValue());
        productService.updateProduct(selectedProduct);

        logger.debug("Editing cell");
    }

    @FXML
    private void editType(TableColumn.CellEditEvent<Product, String> editEvent)
    {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        selectedProduct.setType(editEvent.getNewValue());
        productService.updateProduct(selectedProduct);

        logger.debug("Editing cell");
    }

    @FXML
    private void editQuantity(TableColumn.CellEditEvent<Product, Integer> editEvent)
    {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        Shelf shelf = selectedProduct.getShelf();

        List<Product> products = shelf.getProductList();
        Integer fullness = 0;
        for (Product product : products) {
            fullness += product.getQuantity();
        }
        if (fullness - selectedProduct.getQuantity() + editEvent.getNewValue() <= shelf.getCapacity())
        {
            selectedProduct.setQuantity(editEvent.getNewValue());
            productService.updateProduct(selectedProduct);
        }
        else
        {
            logger.error("MyAddException");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "MyAddException", ButtonType.OK);
            IOAlert.setContentText("MyAddException");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            table.refresh();
        }
        logger.debug("Editing cell");
    }

    @FXML
    private void editShelf(TableColumn.CellEditEvent<Product, Shelf> editEvent)
    {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        Shelf shelf = editEvent.getNewValue();

        List<Product> products = shelf.getProductList();
        Integer fullness = 0;
        for (Product product : products) {
            fullness += product.getQuantity();
        }
        if (shelf.getCapacity() - fullness - selectedProduct.getQuantity() >= 0)
        {
            selectedProduct.setShelf(shelf);
            productService.updateProduct(selectedProduct);
        }
        else
        {
            logger.error("MyAddException");
            Alert IOAlert = new Alert(Alert.AlertType.ERROR, "MyAddException", ButtonType.OK);
            IOAlert.setContentText("No space in this shelf try another one / Or click on refresh");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            table.refresh();
        }

        logger.debug("Editing cell");
    }

    @FXML
    private void editContract(TableColumn.CellEditEvent<Product, Contract> editEvent)
    {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        Contract contract = editEvent.getNewValue();
        selectedProduct.setContract(contract);
        productService.updateProduct(selectedProduct);

        logger.debug("Editing cell");
    }


    @FXML
    private void toPDF(ActionEvent event) throws IOException {
        try {
//            logger.debug("Saving to PDF");
            Document my_pdf_report = new Document();
            PdfWriter.getInstance(my_pdf_report, new FileOutputStream("report_products.pdf"));
            my_pdf_report.open();

            PdfPTable my_report_table = new PdfPTable(5);

            PdfPCell table_cell;
            my_report_table.setHeaderRows(1);

            my_report_table.addCell(new PdfPCell(new Phrase("Name", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Type", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Quantity", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Shelf", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));
            my_report_table.addCell(new PdfPCell(new Phrase("Contract", FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD))));


            if (ProductList.isEmpty()) throw new MyPDFException();

            for(Product products : ProductList)
            {
                table_cell=new PdfPCell(new Phrase(String.valueOf(products.getName())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(products.getType())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(products.getQuantity())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(products.getShelf().getNumber())));
                my_report_table.addCell(table_cell);
                table_cell=new PdfPCell(new Phrase(String.valueOf(products.getContract().getNumber())));
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
    private void refreshScreen(ActionEvent event) throws IOException {
        SceneController.getProductsScene(event);
    }

    @FXML
    private void changeUser(ActionEvent event) throws IOException {
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

        choice_box.setValue("Products");
        choice_box.getItems().addAll(choices);

        setProductList();

        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("type"));
        quantity_column.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        shelf_column.setCellValueFactory(new PropertyValueFactory<>("shelf"));
        contract_column.setCellValueFactory(new PropertyValueFactory<>("contract"));

        ObservableList<Contract> contractsObservableList = FXCollections.observableArrayList(contractService.getContracts());
        ObservableList<Shelf> shelfsObservableList = FXCollections.observableArrayList(shelfService.getShelves());

        table.setEditable(true);

        name_column.setCellFactory(TextFieldTableCell.forTableColumn());
        type_column.setCellFactory(TextFieldTableCell.forTableColumn());
        quantity_column.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        shelf_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(shelfsObservableList));
        contract_column.setCellFactory(ChoiceBoxTableCell.forTableColumn(contractsObservableList));

        table.setItems(getSortedList());
    }
}

