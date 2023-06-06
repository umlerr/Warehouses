package com.umler.warehouses.Services;

import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.Shelf;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для работы с таблицей Shelf в базе данных
 * @author Umler
 */

public class ShelfService {

    /**
     * Создает новый стеллаж или обновляет существующую в базе данных.
     * @param shelf объект, представляющий стеллаж
     * @return true, если операция выполнена успешно, false в противном случае
     */
    public Boolean createShelf(Shelf shelf) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(shelf);
            transaction.commit();
            return transaction.getStatus() == TransactionStatus.COMMITTED;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Обновляет информацию о стеллаже в базе данных.
     * @param shelf объект, представляющий стеллаж с обновленными данными
     */
    public void updateShelf(Shelf shelf) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(shelf);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Удаляет информацию о стеллаже из базы данных.
     * @param shelf объект, представляющий стеллаж, который нужно удалить
     */
    public void deleteShelf(Shelf shelf) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(shelf);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Возвращает стеллаж по номеру.
     * @param number номер стеллажа для поиска в БД.
     * @return стеллаж по номеру
     */
    public Shelf getShelf(Integer number) {
        List<Shelf> shelves = getShelves();
        for (Shelf shelf : shelves){
            if (Objects.equals(shelf.getNumber(), number))
                return shelf;
        }
        return null;
    }

    /**
     * Возвращает список всех стеллажей из базы данных.
     * @return список объектов, представляющих стеллажи
     */
    public List<Shelf> getShelves() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Shelf ", Shelf.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}
