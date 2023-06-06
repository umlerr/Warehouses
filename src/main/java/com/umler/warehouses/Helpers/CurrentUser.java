package com.umler.warehouses.Helpers;

import com.umler.warehouses.Model.User;

/**
 * Класс для сохранения данных о текущем авторизированном пользователе.
 * @author Umler
 */

public class CurrentUser {

    private static User user;

    private CurrentUser() {
    }

    public static User getCurrentUser() {
        return user;
    }

    public static void setCurrentUser(User currentUser) {
        user = currentUser;
    }
}
