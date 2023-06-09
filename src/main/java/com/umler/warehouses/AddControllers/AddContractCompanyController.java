package com.umler.warehouses.AddControllers;

import com.umler.warehouses.Helpers.DatePickerCellFactory;
import com.umler.warehouses.Model.Company;
import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Services.ContractService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;
import com.umler.warehouses.Controllers.SceneController;
import com.umler.warehouses.Helpers.UpdateStatus;
import com.umler.warehouses.Services.CompanyService;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class AddContractCompanyController implements Initializable {

    @FXML
    public TextField name_field;
    @FXML
    public TextField phone_field;
    @FXML
    public TextField tin_field;
    @FXML
    public DatePicker enddatepicker;
    @FXML
    public DatePicker startdate_field;
    @FXML
    public TextField number_field;
    @FXML
    public TextField city_field;
    @FXML
    public TextField street_field;
    @FXML
    public TextField building_field;
    @FXML
    public TextField index_field;
    @FXML
    public ChoiceBox<Company> company_choicebox;

    CompanyService companyService = new CompanyService();

    ContractService contractService = new ContractService();


    /**
     * Сохраняет изменения в базе данных после добавления компании и контракта.
     * Если данные введены корректно, то создается новый объект компании и контракта и происходит обновление базы данных.
     * Если компания уже была создана, и ее выбрали, новый контракт присоеденяется к этой компании.
     * Если данные введены некорректно, то выводится сообщение об ошибке.
     */
    @FXML
    private void saveNewContractCompanyToDb(ActionEvent event){
        if (validateInputs()) {
            int flag = 0;
            Contract contract = createContractFromInput();
            Company company = createCompanyFromInput();

            List<Company> companies = companyService.getCompanies();
            for (Company company1 : companies)
            {
                if (company1.getTin().equals(company.getTin()) &&
                        company1.getName().equals(company.getName())
                        && company1.getPhoneNumber().equals(company.getPhoneNumber())
                        && company1.getAddress().equals(company.getAddress()))
                {
                    flag = 1;
                    company1.addContract(contract);
                    boolean isSaved = new ContractService().createContract(contract);
                    new CompanyService().updateCompany(company1);
                    if (isSaved) {
                        UpdateStatus.setIsContractCompanyAdded(true);
                        delayWindowClose(event);
                    }
                }
            }
            if (flag == 0)
            {
                company.addContract(contract);
                boolean isSaved = new CompanyService().createCompany(company);
                if (isSaved) {
                    UpdateStatus.setIsContractCompanyAdded(true);
                    delayWindowClose(event);
                }
            }
        }
    }

    /**
     * Проверяет корректность введенных данных.
     * Если все поля заполнены корректно, возвращает true.
     * Если есть незаполненные поля или данные введены некорректно, выводит сообщение об ошибке и возвращает false.
     */
    private boolean validateInputs() {
        DatePickerCellFactory enddate_field = new DatePickerCellFactory(enddatepicker);
        Alert IOAlert = new Alert(Alert.AlertType.ERROR, "Input Error", ButtonType.OK);
        if (name_field.getText().equals("")
                || phone_field.getText().equals("")
                || tin_field.getText().equals("")
                || enddate_field.getValue() == null
                || startdate_field.getValue() == null
                || number_field.getText().equals("")
                || city_field.getText().equals("")
                || street_field.getText().equals("")
                || building_field.getText().equals("")
                || index_field.getText().equals("")) {
            IOAlert.setContentText("You must fill out the contract to continue");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (isNumeric(number_field.getText())){
            IOAlert.setContentText("Incorrect input for contract number - you must put a positive number");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (isNumeric(index_field.getText()) || isCorrectIndex(index_field.getText())){
            IOAlert.setContentText("Incorrect input for index number - you must put a number in format XXXYYY");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }

        if (isNumeric(phone_field.getText()) || isCorrectTINorPhone(phone_field.getText())){
            IOAlert.setContentText("Incorrect input for phone number - you must put a number with 10 symbols");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (!isNumberExist(Integer.valueOf(number_field.getText()))){
            IOAlert.setContentText("Incorrect input for CONTRACT NUMBER - CONTRACT with this number already exists");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        if (isNumeric(tin_field.getText()) || isCorrectTINorPhone(tin_field.getText())){
            IOAlert.setContentText("Incorrect input for TIN - you must put a number with 10 symbols");
            IOAlert.showAndWait();
            if(IOAlert.getResult() == ButtonType.OK)
            {
                IOAlert.close();
            }
            return false;
        }
        return true;
    }

    /**
     * Создает новый объект контракта на основе введенных данных.
     * @return новый объект контракта
     */
    private Contract createContractFromInput() {
        DatePickerCellFactory enddate_field = new DatePickerCellFactory(enddatepicker);
        Contract contract = new Contract();
        contract.setStartdate(startdate_field.getValue());
        contract.setEnddate(enddate_field.getValue());
        contract.setNumber(Integer.valueOf(number_field.getText()));
        return contract;
    }

    /**
     * Создает новый объект компании на основе введенных данных.
     * @return новый объект компании
     */
    private Company createCompanyFromInput() {
        Company company = new Company();
        company.setName(name_field.getText());
        company.setAddress(capitalizeAfterDot(capitalize(city_field.getText().toLowerCase())) + ", st." +
                capitalize(street_field.getText().toLowerCase()) + ", " +
                building_field.getText() +
                ", " + index_field.getText());
        company.setPhoneNumber(phone_field.getText());
        company.setTin(tin_field.getText());
        return company;
    }

    /**
     * Делает первую букву строки прописной.
     * @return строку с большой буквы
     */
    private static String capitalize(String str)
    {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Делает букву после точки прописной.
     * @return строку с большой буквы после точки для проверки городов по типу St.Petersburg
     */
    private static String capitalizeAfterDot(String str) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (capitalizeNext && Character.isLetter(c)) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
            if (c == '.') {
                capitalizeNext = true;
            }
        }
        return result.toString();
    }

    /**
     * Выбор уже существующей компании.
     * Блокировка полей если компания выбрана
     */
    @FXML
    private void getChoices() {
        Company company = company_choicebox.getValue();
        if (!Objects.equals(company, null))
        {
            String[] parts = company.getAddress().replaceAll("\\s+","").split(",");
            name_field.setEditable(false);
            name_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            name_field.setText(company.getName());
            city_field.setEditable(false);
            city_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            city_field.setText(parts[0]);
            street_field.setEditable(false);
            street_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            street_field.setText(parts[1].substring(3));
            building_field.setEditable(false);
            building_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            building_field.setText(parts[2]);
            index_field.setEditable(false);
            index_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            index_field.setText(parts[3]);
            phone_field.setEditable(false);
            phone_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            phone_field.setText(company.getPhoneNumber());
            tin_field.setEditable(false);
            tin_field.setStyle("-fx-opacity: 0.7; -fx-background-color: #eee");
            tin_field.setText(company.getTin());
        }
        else
        {
            name_field.setEditable(true);
            name_field.setStyle(null);
            city_field.setEditable(true);
            city_field.setStyle(null);
            street_field.setEditable(true);
            street_field.setStyle(null);
            building_field.setEditable(true);
            building_field.setStyle(null);
            index_field.setEditable(true);
            index_field.setStyle(null);
            phone_field.setEditable(true);
            phone_field.setStyle(null);
            tin_field.setEditable(true);
            tin_field.setStyle(null);
        }
    }

    /**
     * Проверка введенной строки на число.
     */
    private static boolean isNumeric(String str) {
        try {
            return Double.parseDouble(str) <= 0;
        } catch(NumberFormatException e){
            return true;
        }
    }

    /**
     * Проверка введеного почтового индекса на кол-во символов.
     */
    private static boolean isCorrectIndex(String str) {
        try {
            return str.length()!=6;
        } catch(NumberFormatException e){
            return true;
        }
    }

    /**
     * Проверка введеного ИНН на кол-во символов.
     */
    private static boolean isCorrectTINorPhone(String str) {
        try {
            return str.length()!=10;
        } catch(NumberFormatException e){
            return true;
        }
    }

    /**
     * Проверка что введеный номер контракта уже существует.
     */
    private boolean isNumberExist(Integer number){
        for (Contract contracts : contractService.getContracts()){
            if (Objects.equals(contracts.getNumber(), number))
                return false;
        }
        return true;
    }

    /**
     * Задержка закрытия окна добавления до нажатия клавишы сохранения.
     */
    private void delayWindowClose(ActionEvent event) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event2 -> closeWindow(event));
        delay.play();
    }

    /**
     * Закрытия окна по кнопке.
     */
    @FXML
    private void closeWindow(ActionEvent event) {
        SceneController.close(event);
    }

    /**
     * Инициализация.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        startdate_field.setEditable(false);
        startdate_field.setShowWeekNumbers(false);
        startdate_field.setStyle("-fx-opacity: 0.9;");
        startdate_field.setValue(LocalDate.now());

        DatePickerCellFactory enddate_field = new DatePickerCellFactory(enddatepicker);

        ObservableList<Company> roomObservableList = FXCollections.observableArrayList(companyService.getCompanies());
        roomObservableList.add(null);
        company_choicebox.getItems().addAll(roomObservableList);
    }
}
