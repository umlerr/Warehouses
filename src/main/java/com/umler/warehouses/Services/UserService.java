package com.umler.warehouses.Services;

import com.umler.warehouses.Model.User;
import com.umler.warehouses.Helpers.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;


/**
 * Класс для работы с таблицей User в базе данных
 * @author Umler
 */

public class UserService {

    /**
     * Возвращает данные пользователя по введным логину и паролю.
     * @return данные о пользователе
     */
    public User getConnectedUser(String name, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TypedQuery<User> query = session.createQuery("SELECT u FROM User u " +
                    "WHERE name = :name AND password = :pass", User.class);
            query.setParameter("name", name);
            query.setParameter("pass", password);
            return query.getSingleResult();
        } catch (NoResultException ex) {
            System.err.println("User not found");
            return null;
        }
    }
}
