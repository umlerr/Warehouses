package com.umler.warehouses.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.umler.warehouses.Helpers.CurrentUser;
import com.umler.warehouses.Model.User;
import com.umler.warehouses.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Контроллер для таблицы пользователей.
 * @author Umler
 */
public class LoginController implements Initializable {

    public Button LoginButton;

    public Button exit_btn;

    public Button wrap_btn;

    @FXML
    private Label invalid_label;

    @FXML
    private TextField username_box;

    @FXML
    private TextField password_box;

    UserService userService = new UserService();

    private static final Logger logger = LoggerFactory.getLogger("LoginScene Logger");

    /**
     * Обработчик события открытия главного интерфейса программы.
     * Получает выбранное значение и вызывает соответствующий метод в классе SceneController для отображения соответствующей сцены.
     * @param event событие открытия главного интерфейса программы.
     * @throws IOException если возникает ошибка ввода-вывода при отображении сцены.
     */
    @FXML
    void showVisitScreen(ActionEvent event) throws IOException {
        if (validateLogin())
        {
            SceneController.getContractsScene(event);
        }
        else
        {

            logger.warn("Invalid credentials");

            username_box.clear();
            password_box.clear();
            invalid_label.setText("Invalid credentials");
        }

    }

    /**
     * Проверяет корректность введенных данных.
     * Если введеные данные для входа есть в базе, возвращает true.
     * Если есть незаполненные поля или данные введенные не соответсвуют БД, выводит сообщение об ошибке и возвращает false.
     */
    private boolean validateLogin() {
        User user = userService.getConnectedUser(username_box.getText(), password_box.getText());
        if (user == null) {
            return false;
        }
        CurrentUser.setCurrentUser(user);
        return true;
    }

    /**
     * Выход из окна программы.
     */
    public void ExitLoginWindow() {
        logger.debug("Closing login window");
        exit_btn.setOnAction(SceneController::close);
    }

    /**
     * Сворачивание окна программы.
     */
    public void WrapLoginWindow() {
        logger.debug("Wrapping login window");
        wrap_btn.setOnAction(SceneController::wrap);
    }

    /**
     * Инициализация.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}